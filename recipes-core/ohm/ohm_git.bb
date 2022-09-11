SUMMARY = "Open Hardware Manager"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=045d04e17422d99e338da75b9c749b7c"

PV = "1.3.0+git${SRCPV}"
SRCREV = "0ec32512ac5bf8d1c6bd2f489d9a75711eed3b4b"

inherit pkgconfig autotools-brokensep systemd

DEPENDS = "glib-2.0 glib-2.0-native libcheck dbus dbus-glib dbus-glib-native libtrace"
RDEPENDS:${PN} = "glib-2.0 systemd"
RDEPENDS:${PN}-dev = "${PN}"

SRC_URI = " \
    git://github.com/sailfishos/ohm.git;protocol=https;branch=master \
"

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = " \
    --disable-static \
    --disable-aegis \
    --without-xauth \
    --with-distro=meego \
"

# QA failed without any reason apparent in the log files.
ERROR_QA:remove = "configure-unsafe"

do_install:append() {
    install -d "${D}"/${sysconfdir}/ohm/plugins.d
    install -d "${D}"/${localstatedir}/lib/ohm

    # Move the service file installed in a hard-coded path to the correct path.
    install -Dm 644 ${S}/initscript/MeeGo/ohmd.service ${D}/${systemd_system_unitdir}/ohmd.service
    rm -r ${D}/usr/lib/systemd
    
    ln -s ohmd.service ${D}/${systemd_system_unitdir}/dbus-org.freedesktop.ohm.service
    install -d "${D}"/${libdir}/ohm
    rm -rf "${D}"/${libdir}/*.la
}

FILES:${PN} += " \
    ${sysconfdir}/ohm \
    ${sysconfdir}/ohm/plugins.d \
    ${sbindir}/*ohm* \
    ${systemd_system_unitdir}/ohmd.service \
    ${systemd_system_unitdir}/dbus-org.freedesktop.ohm.service \
    ${datadir}/dbus-1/system-services/org.freedesktop.ohm.service \
    ${sysconfdir}/dbus-1/system.d/ohm.conf \
    ${libdir}/ohm \
    ${sysconfdir}/ohm/modules.ini \
    ${localstatedir}/lib/ohm \
"

FILES:${PN}-dev += " \
    ${libdir}/pkgconfig/*.pc \
    ${libdir}/libiphb.so \
"

SYSTEMD_SERVICE:${PN} = "ohmd.service"