SUMMARY = "API for IP Heartbeat service"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "1.2.7+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "c7fd4ea12063de898cd9436fb982ecdb5103c73e"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

inherit pkgconfig autotools-brokensep

DEPENDS = "glib-2.0 libcheck dbus libdsme mce-headers"
RDEPENDS:${PN} = "glib-2.0"
RDEPENDS:${PN}-dev = "${PN}"

SRC_URI = " \
    git://github.com/sailfishos/libiphb.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = " \
    --disable-static \
"

do_install() {
    cd "${S}"
    make DESTDIR="${D}" install

    rm -f "${D}${libdir}/*.la"
    rm -rf "${D}/opt"
}

FILES:${PN} += " \
    ${libdir}/libiphb.so.* \
"

FILES:${PN}-dev += " \
    ${libdir}/libiphb.so \
    ${libdir}/pkgconfig/libiphb.pc \
    ${includedir}/iphbd/* \
"