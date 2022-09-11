SUMMARY = "A simple tracing library with keyword filtering support"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "1.1.8+git4"
SRCREV = "02376f90b19da759d94d70b3833b8ddd46c9ad75"

inherit pkgconfig autotools-brokensep

SRC_URI = " \
    git://github.com/sailfishos/libtrace-ohm.git;protocol=https;branch=master \
"

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = " \
    --disable-static \
"

do_install:append() {
    rm -rf "${D}"/${libdir}/*.la
}

FILES:${PN} += " \
    ${libdir}/libsimple-trace*.so.* \
"

FILES:${PN}-dev += " \
    ${libdir}/pkgconfig/*.pc \
    ${libdir}/libsimple-trace*.so \
    %{includedir}/simple-trace \
"
