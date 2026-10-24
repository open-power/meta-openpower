FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:powernv = " file://0001-shutdown-Call-kexec-with-f-option.patch"
