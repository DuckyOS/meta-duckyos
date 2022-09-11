SUMMARY = "DSME dsmesock dynamic library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

PV = "0.66.7+git${SRCPV}"
SRCREV = "f171e17411385cc25b67a7d0c608597824c8fdf1"

DEPENDS = "glib-2.0 libcheck"
RDEPENDS:${PN} = "glib-2.0"
RDEPENDS:${PN}-dev = "${PN}"

inherit pkgconfig

SRC_URI = " \
    git://github.com/sailfishos/libdsme.git;protocol=https;branch=master \
"

S = "${WORKDIR}/git"

do_compile:prepend() {
    cd "${S}"
    ./verify_version
}

do_install() {
    cd "${S}"
    make DESTDIR="${D}" install_main install_devel

    # Fix symlinks
    for name in libdsme libdsme_dbus_if libthermalmanager_dbus_if; do
        ln -sf $name.so.0.3.0 "${D}"/usr/lib/$name.so.0
        ln -sf $name.so.0.3.0 "${D}"/usr/lib/$name.so
    done
}

FILES:${PN} += " \
    ${libdir}/libdsme.so.* \
    ${libdir}/libdsme_dbus_if.so.* \
    ${libdir}/libthermalmanager_dbus_if.so.* \
"

FILES:${PN}-dev += " \
    ${libdir}/libdsme.so \
    ${libdir}/libdsme_dbus_if.so \
    ${libdir}/libthermalmanager_dbus_if.so \
    ${libdir}/pkgconfig/dsme.pc \
    ${includedir}/dsme/* \
"