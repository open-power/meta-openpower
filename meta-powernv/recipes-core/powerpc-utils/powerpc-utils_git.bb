SUMMARY = "System utilities for PowerPC machines"
HOMEPAGE = "https://github.com/ibm-power-utilities/powerpc-utils"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS  = "numactl zlib"
RDEPENDS:${PN} = "bash perl"

SRC_URI = "git://github.com/ibm-power-utilities/powerpc-utils.git;branch=next;protocol=https"
SRCREV = "56f98cdf411196ea19336b3d5afe2382c596752a"
PV = "1.3.13+git"

inherit autotools

EXTRA_OECONF = "--without-librtas"

PACKAGES =+ "${PN}-nvram"
FILES:${PN}-nvram = "${sbindir}/nvram"
