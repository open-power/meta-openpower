SUMMARY = "A boot image that runs petitboot"

PACKAGE_INSTALL = "\
    packagegroup-core-boot \
    petitboot \
    ${PETITBOOT_IMAGE_EXTRA_INSTALL} \
"

# Helpful when using petitboot's shell as a system recovery/diagnostic environment
PETITBOOT_IMAGE_EXTRA_INSTALL = "\
    dropbear \
    ethtool \
    ipmitool \
    lrzsz \
    netcat \
    pciutils \
    rsync \
"

PACKAGE_EXCLUDE = "kernel-image-*"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX = ""

IMAGE_FEATURES = "empty-root-password"
IMAGE_LINGUAS = "en-us de-de es-es fr-fr it-it ja-jp ko-kr pt-br ru-ru zh-cn zh-tw"

rootfs_postprocess() {
    echo "LANG=en_US.UTF-8" > ${IMAGE_ROOTFS}/etc/locale.conf

    # pb-console looks in locale instead of locale.conf
    ln -s locale.conf ${IMAGE_ROOTFS}/etc/locale

    # Avoid "Warning: unable to open an initial console" at boot
    install -d ${IMAGE_ROOTFS}/dev
    mknod -m 600 ${IMAGE_ROOTFS}/dev/console c 5 1

    # We already run pb-console on serial consoles, don't run getty
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/inittab ]; then
        sed -i "/start_getty/d" ${IMAGE_ROOTFS}${sysconfdir}/inittab
    fi
}

ROOTFS_POSTPROCESS_COMMAND += "rootfs_postprocess; "

inherit core-image
