# Filter firmware binaries to save space

FILES:${PN}-bnx2x:petitroot = "${nonarch_base_libdir}/firmware/bnx2x/bnx2x-e2-7.13.21.0.fw"

FILES:${PN}-cxgb4:petitroot = "\
    ${nonarch_base_libdir}/firmware/cxgb4/t4fw-1.27.5.0.bin \
    ${nonarch_base_libdir}/firmware/cxgb4/t4fw.bin \
"

INSANE_SKIP:${PN}:petitroot = "installed-vs-shipped"
