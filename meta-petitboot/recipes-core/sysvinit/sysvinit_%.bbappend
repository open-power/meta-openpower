FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Silence /init itself
SRC_URI:append:petitroot = " file://0001-init-Disable-console-output.patch"

do_install:append:petitroot() {
    # Silence everything launched via /etc/init.d/rc 
    printf "\nexec >/dev/null 2>&1\n" >> ${D}${sysconfdir}/default/rcS

    # bootlogd doesn't play nice with the pb-console ui
    echo "BOOTLOGD_ENABLE=n" >> ${D}${sysconfdir}/default/bootlogd
}
