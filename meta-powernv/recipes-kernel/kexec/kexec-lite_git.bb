SUMMARY = "A simple kexec for flattened device tree platforms"

DESCRIPTION = "\
   Kexec is a userspace utility for loading another kernel and asking the \
   currently running kernel to do something with it. \
   \
   This package is a tiny implementation of the kexec userspace components, \
   for devicetree-based platforms. \
"

HOMEPAGE = "https://github.com/antonblanchard/kexec-lite"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS  = "dtc elfutils"

SRC_URI = "git://github.com//antonblanchard/kexec-lite.git;branch=master;protocol=https"

SRCREV = "6b0130b3c1ea489e061cda2805e6f8b68dc96a76"
PV = "0.1+git"

inherit autotools

RPROVIDES:${PN} += "virtual-kexec"
