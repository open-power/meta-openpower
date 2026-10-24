# Put the server in an optionally-built subpackage

PACKAGECONFIG[server] = ","

PACKAGECONFIG:append = " server"
PACKAGECONFIG:remove:petitroot = "server"

SBINCOMMANDS:remove = "${@bb.utils.contains('PACKAGECONFIG', 'server', '', 'dropbear', d)}"

do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'server', 'false', 'true', d)}; then
        rm ${D}${sysconfdir}/init.d/dropbear
    fi
}

PACKAGES =+ "${@bb.utils.contains('PACKAGECONFIG', 'server', '${PN}-server', '', d)}"

FILES:${PN}-server = "\
    ${sbindir}/dropbear \
    ${sysconfdir}/init.d \
    ${systemd_system_unitdir} \
"

INITSCRIPT_PACKAGES = "${PN}-server"
INITSCRIPT_NAME = ""
INITSCRIPT_PARAMS = ""
INITSCRIPT_NAME:${PN}-server = "dropbear"
INITSCRIPT_PARAMS:${PN}-server = "defaults 10"

SYSTEMD_SERVICE:${PN} = ""
SYSTEMD_SERVICE:${PN}-server = "dropbear.socket"
