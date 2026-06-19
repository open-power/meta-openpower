SOC_FAMILY = "powernv"

require conf/machine/include/soc-family.inc
require conf/machine/include/powerpc/arch-powerpc64.inc

DEFAULTTUNE = "powerpc64le"

PREFERRED_PROVIDER_virtual/bootloader ?= "skiboot"
EXTRA_IMAGEDEPENDS += "virtual/bootloader"
SKIBOOT_IMAGETYPES = "lid lid.xz"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-openpower"
KERNEL_IMAGETYPE = "zImage.epapr"
INITRAMFS_FSTYPES = "cpio.xz"

PREFERRED_RPROVIDER_virtual-kexec = "kexec-lite"
