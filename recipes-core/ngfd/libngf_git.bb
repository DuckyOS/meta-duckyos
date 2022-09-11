SUMMARY = "Non-graphic feedback C-based client library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "0.28+git${SRCPV}"
SRCREV = "05ea200943795ae343bad5748ae125431b3b9837"

DEPENDS = "glib-2.0 libcheck dbus dbus-glib mce-headers"

SRC_URI = " \
    git://github.com/sailfishos/libngf.git;protocol=https;branch=master \
"

inherit pkgconfig autotools-brokensep

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = "--prefix=/usr \
    --disable-static \
"

do_install() {
    cd "${S}"
    make DESTDIR="${D}" install
}

FILES:${PN} += " \
    ${libdir}/libngf0-*.so.* \
    ${bindir}/ngf-client \
"

FILES:${PN}-dev += " \
    ${includedir}/libngf-1.0/libngf/* \
    ${libdir}/libngf0-*.so \
    ${libdir}/pkgconfig/libngf0.pc \
"