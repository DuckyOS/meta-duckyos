SUMMARY = "MeeGo resource management low level C API libraries"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "0.25.1+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "60fdabae78070ef6c70351e5c5b0a5861229d926"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "dbus-glib libcheck"

SRC_URI = " \
    git://github.com/sailfishos/libresource.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"
S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = " \
    --disable-static \
"

PARALLEL_MAKE = "-j1"

inherit pkgconfig autotools-brokensep

FILES:${PN} += " \
    ${libdir}/libresource.so.* \
"

FILES:${PN}-dev += " \ 
    ${libdir}/libresource.so \
    ${libdir}/pkgconfig/libresource*.pc \
    ${includedir}/libresource*/*.h \
"
