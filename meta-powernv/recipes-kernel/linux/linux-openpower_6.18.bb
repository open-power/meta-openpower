SUMMARY = "A kernel with simple config for running petitboot"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "\
    https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${LINUX_VERSION}.tar.xz \
    file://0001-xhci-Reset-controller-on-xhci-shutdown.patch \
"

SRC_URI[sha256sum] = "a83cd200e6646db52866b8309e9137b9e9048b613cbda10ced2b811aae125255"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.18.37"
LINUX_VERSION_EXTENSION = "-openpower"

PV = "${LINUX_VERSION}"
S = "${UNPACKDIR}/linux-${PV}"

COMPATIBLE_MACHINE = "^powernv$"

KBUILD_DEFCONFIG = "skiroot_defconfig"
KCONFIG_MODE = "alldefconfig"

# ELF binary has relocations in .text
INSANE_SKIP:kernel-vmlinux = "textrel"
INSANE_SKIP:kernel-image-zimage.epapr = "textrel"
