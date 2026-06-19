SUMMARY = "Petitboot bootloader"
DESCRIPTION = "Petitboot is a small kexec-based bootloader."
HOMEPAGE = "https://github.com/open-power/petitboot"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "\
    git://github.com/open-power/petitboot.git;branch=master;protocol=https \
    file://0001-pb-console-Increase-boot-timeout.patch \
    file://getty-override.conf \
    file://kexec \
    file://kexec-restart \
    file://pb-console \
    file://pb-discover \
    file://pb-discover.service \
    file://pb-shell \
    file://serial-getty-override.conf \
    file://shell_profile \
    file://tmpfiles.conf \
"

DEPENDS = "\
    bison-native \
    elfutils \
    flex-native \
    libdevmapper \
    ncurses \
    udev \
"

RDEPENDS:${PN} = "\
    kexec-reboot \
    libdevmapper \
    libudev \
    ncurses-libform \
    ncurses-libmenu \
    ncurses-libncurses \
    ncurses-libtinfo \
    ncurses-terminfo-base \
    udev \
    util-linux-agetty \
    virtual-kexec \
"

RRECOMMENDS:${PN} = "\
    cryptsetup \
    keyutils \
    mdadm \
"

SRCREV = "96063aa2d2d8795389024d05967c3f4bcbbf9aab"
PV = "1.15+git"

inherit autotools-brokensep gettext pkgconfig systemd update-rc.d useradd

PACKAGECONFIG[busybox] = "--enable-busybox, --disable-busybox"
PACKAGECONFIG[fdt] = "--with-fdt, --without-fdt, dtc, dtc"
PACKAGECONFIG[tftp-busybox] = "--with-tftp=busybox, , , , , tftp-hpa"
PACKAGECONFIG[tftp-hpa] = "--with-tftp=hpa, , , tftp-hpa, , tftp-busybox"

PACKAGECONFIG ??= "${@bb.utils.contains('VIRTUAL-RUNTIME_base-utils', 'busybox', 'busybox tftp-busybox', '', d)}"

EXTRA_OECONF = "\
    --enable-crypt \
    --enable-platform-auto \
    --with-ncurses \
    --without-signed-boot \
    --without-twin-fbdev \
    --without-twin-x11 \
    HOST_PROG_KEXEC=${sbindir}/kexec \
    HOST_PROG_SH=${libexecdir}/petitboot/pb-shell \
    HOST_PROG_SHUTDOWN=${libexecdir}/petitboot/kexec-restart \
"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r petitgroup"
USERADD_PARAM:${PN} = "-r -g petitgroup -d /home/petituser -m -s /bin/sh petituser"

PETITBOOT_GETTY_PORT ??= "console"

do_configure:prepend() {
    ./bootstrap
}

do_install:append() {
    install -D -m 0755 ${UNPACKDIR}/kexec-restart \
        ${D}${libexecdir}/petitboot/kexec-restart
    install -D -m 0755 ${UNPACKDIR}/pb-shell \
        ${D}${libexecdir}/petitboot/pb-shell
    install -D -m 0644 -o petituser -g petitgroup ${UNPACKDIR}/shell_profile \
        ${D}/home/petituser/.profile
    install -D -m 0644 -o petituser -g petitgroup /dev/null \
        ${D}/home/petituser/.hushlogin

    if ${@bb.utils.contains('PACKAGECONFIG', 'busybox', 'true', 'false', d)}; then
        install -d ${D}${datadir}/udhcpc/default.script.d
        ln -rs ${D}${sbindir}/pb-udhcpc \
            ${D}${datadir}/udhcpc/default.script.d/pb-udhcpc
    fi

    if ${@bb.utils.contains('PACKAGECONFIG', 'fdt', 'true', 'false', d)}; then
        install -D -m 0755 ${S}/utils/hooks/01-create-default-dtb \
            ${D}${sysconfdir}/petitboot/boot.d/01-create-default-dtb
        install -D -m 0755 ${S}/utils/hooks/30-dtb-updates \
            ${D}${sysconfdir}/petitboot/boot.d/30-dtb-updates
        install -D -m 0755 ${S}/utils/hooks/90-sort-dtb \
            ${D}${sysconfdir}/petitboot/boot.d/90-sort-dtb
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
        sed -i -e 's,@KEXEC_COMMAND@,${base_sbindir}/shutdown -r now,' \
            ${D}${libexecdir}/petitboot/kexec-restart
        install -D -m 0755 ${UNPACKDIR}/kexec \
            ${D}${sysconfdir}/init.d/kexec
        install -D -m 0755 ${UNPACKDIR}/pb-console \
            ${D}${sysconfdir}/init.d/pb-console
        install -D -m 0755 ${UNPACKDIR}/pb-discover \
            ${D}${sysconfdir}/init.d/pb-discover

        install -d ${D}${sysconfdir}/udev/rules.d
        echo "${PETITBOOT_GETTY_PORT}" | xargs printf \
            'SUBSYSTEM=="tty", KERNEL=="%s", RUN+="${sysconfdir}/init.d/pb-console start $name"\n' \
        > ${D}${sysconfdir}/udev/rules.d/petitboot-console-ui.rules
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        sed -i -e 's,@KEXEC_COMMAND@,${base_bindir}/systemctl kexec,' \
            ${D}${libexecdir}/petitboot/kexec-restart
        install -D -m 0644 ${UNPACKDIR}/getty-override.conf \
            ${D}${systemd_system_unitdir}/getty@.service.d/pb-console.conf
        install -D -m 0644 ${UNPACKDIR}/serial-getty-override.conf \
            ${D}${systemd_system_unitdir}/serial-getty@.service.d/pb-console.conf
        install -D -m 0644 ${UNPACKDIR}/pb-discover.service \
            ${D}${systemd_system_unitdir}/pb-discover.service
        sed -i -e 's,@SBINDIR@,${sbindir},g' \
            ${D}${systemd_system_unitdir}/pb-discover.service
        install -D -m 644 ${UNPACKDIR}/tmpfiles.conf \
            ${D}${sysconfdir}/tmpfiles.d/petitboot.conf
    fi
}

FILES:${PN} += "\
    ${datadir}/udhcpc/default.script.d/pb-udhcpc \
    ${systemd_system_unitdir}/getty@.service.d/pb-console.conf \
    ${systemd_system_unitdir}/serial-getty@.service.d/pb-console.conf \
    /home/petituser \
"

SYSTEMD_SERVICE:${PN} = "pb-discover.service"

INITSCRIPT_NAME:${PN} = "pb-discover"
INITSCRIPT_PARAMS:${PN} = "start 40 S ."

PACKAGES =+ "kexec-reboot"
ALLOW_EMPTY:kexec-reboot = "1"
FILES:kexec-reboot = "${sysconfdir}/init.d/kexec"

INITSCRIPT_PACKAGES = "${PN} kexec-reboot"
INITSCRIPT_NAME:kexec-reboot = "kexec"
INITSCRIPT_PARAMS:kexec-reboot = "start 89 6 ."
