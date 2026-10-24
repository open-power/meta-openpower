FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:petitroot = " file://petitboot.cfg"
