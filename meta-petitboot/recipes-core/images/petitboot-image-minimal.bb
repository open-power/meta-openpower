require petitboot-image.bb

# Strip optional packages
PETITBOOT_IMAGE_EXTRA_INSTALL = ""
NO_RECOMMENDATIONS = "1"

# Reinject machine essentials
PACKAGE_INSTALL += "${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"

# Strip language catalogs
IMAGE_LINGUAS = "en-us"
