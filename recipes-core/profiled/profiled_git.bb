SUMMARY = "Profile daemon, manages user settings"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=93bc4a25d753385a799547a0560d7001"

PV = "1.0.12+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "d3d2b21a546ef1882a0773a930a66df94c21d9a7"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "dbus-glib dbus doxygen-native"

SRC_URI = " \
    git://github.com/sailfishos/profiled.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"
S = "${WORKDIR}/git"

inherit pkgconfig systemd

do_install() {
    cd "${S}"
    make ROOT="${D}" install-profiled
    make ROOT="${D}" install-libprofile
    make ROOT="${D}" install-libprofile-dev
    make ROOT="${D}" install-libprofile-doc
    make ROOT="${D}" install-profileclient
    rm ${D}/${libdir}/libprofile.a
}

FILES:${PN} += " \
    ${bindir}/profiled \
    ${libdir}/libprofile.so.* \
    ${datadir}/dbus-1/services/com.nokia.profiled.service \
    ${systemd_user_unitdir}/profiled.service \
    ${sysconfdir}/profiled/10.meego_default.ini \
"

FILES:${PN}-dev += " \ 
    ${libdir}/libprofile.so \
    ${libdir}/pkgconfig/profile.pc \
    ${includedir}/profiled/* \
"
