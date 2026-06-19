RDEPENDS:${PN} += " powerpc-utils-nvram"

PACKAGECONFIG[mtd] = "--enable-mtd, , skiboot, libflash"
PACKAGECONFIG:append = " fdt mtd"
