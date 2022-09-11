SUMMARY = "Development files for mce"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

PV = "1.30.0+git${SRCPV}"
SRCREV = "14f1508a42bf128e697c17ecffdbdbe30418ad79"

inherit pkgconfig

SRC_URI = " \
    git://github.com/sailfishos/mce-dev.git;protocol=https;branch=master \
"

S = "${WORKDIR}/git"

do_install() {
    cd "${S}"
    make DESTDIR="${D}" install
}

FILES:${PN} += " \
    ${libdir}/pkgconfig/mce.pc \
    ${includedir}/mce/* \
"