SUMMARY = "skiboot firmware"
DESCRIPTION = "Skiboot is the OPAL (OpenPower Abstraction Layer) boot and runtime \
               firmware for Power systems."
HOMEPAGE = "https://github.com/open-power/skiboot"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENCE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/open-power/skiboot.git;branch=master;protocol=https"
SRCREV = "dbf9ed5346261f41c603a783dd37514376319a4e"
PV = "7.1+git"

COMPATIBLE_MACHINE = "^powernv$"
PROVIDES += "virtual/bootloader"

SKIBOOT_IMAGETYPES ?= "lid"

inherit deploy

EXTRA_OEMAKE += " \
    CROSS=${TARGET_PREFIX} \
    V=1 \
"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    oe_runmake -C ${B} $(printf "skiboot.%s " ${SKIBOOT_IMAGETYPES})

    oe_runmake -C ${B}/external/shared
    oe_runmake -C ${B}/external/gard
    oe_runmake -C ${B}/external/pflash LINKAGE=dynamic
}

do_install() {
    oe_runmake -C ${B}/external/shared DESTDIR=${D} PREFIX=/usr LIBDIR=${libdir} install
    oe_runmake -C ${B}/external/gard DESTDIR=${D} prefix=/usr install
    oe_runmake -C ${B}/external/pflash DESTDIR=${D} install
}

do_deploy() {
    install -d ${DEPLOYDIR}

    for type in ${SKIBOOT_IMAGETYPES}; do
        image=skiboot.$type
        suffix=${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}

        install -m 0644 ${B}/$image ${DEPLOYDIR}/$image-$suffix
        ln -sf $image-$suffix ${DEPLOYDIR}/$image
    done
}

do_deploy[vardepsexclude] = "DATETIME"

# We deploy to filenames that include PKGV and PKGR, read the saved data to
# ensure we get the right values for both
do_deploy[prefuncs] += "read_subpackage_metadata"

addtask deploy after do_packagedata

PACKAGES =+ "libflash"
FILES:libflash = "${libdir}/lib*.so.*"

# libflash.so.x.y.z is built with '-Wl,-soname,libflash.so', so it is a runtime dependency
FILES:libflash += "${libdir}/lib*.so"
INSANE_SKIP:libflash = "dev-so"

PACKAGES =+ "pflash"
RDEPENDS:pflash = "libflash"
FILES:pflash = "${sbindir}/pflash"

PACKAGES =+ "gard"
RDEPENDS:gard = "libflash"
FILES:gard = "${sbindir}/opal-gard"
