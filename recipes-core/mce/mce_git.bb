SUMMARY = "Mode Control Entity for Nokia mobile computers"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "1.111.0+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "2ef8bf19d2ba34b2ad599b709b5cac3dc03d7d4d"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "glib-2.0 libcheck mce-headers libdsme usb-moded systemd doxygen-native libngf libiphb"
RDEPENDS:${PN} = "glib-2.0 libngf libiphb"
RDEPENDS:${PN}-dev = "${PN}"

SRC_URI = " \
    git://github.com/sailfishos/mce.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"

inherit systemd pkgconfig

S = "${WORKDIR}/git"

do_compile:prepend() {
    cd "${S}"
    ./verify_version
}

do_install() {
    cd "${S}"
    make DESTDIR="${D}" install ENABLE_MANPAGE_INSTALL=n

    mkdir -p "${D}"/${sysconfdir}/tmpfiles.d
    echo "d /run/mce 0755 root root -" > "${D}"/${sysconfdir}/tmpfiles.d/mce.conf

    sed -i '/Before=basic.target/d' ${D}/${systemd_system_unitdir}/mce.service

    rm -rf "${D}"/lib/systemd/system/multi-user.target.wants
    rm -rf "${D}"/var "${D}"/run 
}

FILES:${PN} += " \
    ${sbindir}/mce \
    ${sbindir}/dummy_compositor \
    ${libdir}/mce/modules/*.so \
    ${sysconfdir}/mce/*.ini \
    ${sysconfdir}/dbus-1/system.d/mce.conf \
    ${sysconfdir}/tmpfiles.d/mce.conf \
    ${systemd_system_unitdir}/mce.service \
    ${sbindir}/mcetool \
    ${sbindir}/evdev_trace \
"

SYSTEMD_SERVICE:${PN} = "mce.service"