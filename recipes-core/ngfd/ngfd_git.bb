SUMMARY = "Non-graphic feedback service for sounds and other events"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "1.4.0+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "76ef89988129bff9ea439cb5d5684146fdd31869"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "glib-2.0 libcheck dbus dbus-glib mce-headers pulseaudio gstreamer1.0 profiled libcanberra libtool systemd ohm doxygen-native"
RDEPENDS:${PN} = "systemd gstreamer1.0-plugins-good"

SRC_URI = " \
    git://github.com/sailfishos/ngfd.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
"

inherit pkgconfig autotools-brokensep systemd

S = "${WORKDIR}/git"

do_install:append() {
    rm -f ${D}/${libdir}/ngf/*.la
    rm ${D}/${libdir}/ngf/libngfd_fake.so
    rm ${D}/${libdir}/ngf/libngfd_test_fake.so
    rm -r ${D}/opt

    install -d ${D}/${systemd_user_unitdir}
    sed 's/WantedBy=user-session.target/WantedBy=default.target/' \
        ${S}/rpm/ngfd.service > "${D}/${systemd_user_unitdir}/ngfd.service"
}

pkg_postinst_ontarget:${PN}:append() {
    #!/bin/sh -e
    systemctl enable --global ngfd.service
}

FILES:${PN} += " \
    ${bindir}/ngfd \
    ${sysconfdir}/dbus-1/system.d/ngfd.conf \
    ${libdir}/ngf/libngfd_dbus.so \
    ${libdir}/ngf/libngfd_resource.so \
    ${libdir}/ngf/libngfd_transform.so \
    ${libdir}/ngf/libngfd_gst.so \
    ${libdir}/ngf/libngfd_canberra.so \
    ${libdir}/ngf/libngfd_mce.so \
    ${libdir}/ngf/libngfd_streamrestore.so \
    ${libdir}/ngf/libngfd_tonegen.so \
    ${libdir}/ngf/libngfd_callstate.so \
    ${libdir}/ngf/libngfd_profile.so \
    ${libdir}/ngf/libngfd_ffmemless.so \
    ${libdir}/ngf/libngfd_devicelock.so \
    ${libdir}/ngf/libngfd_route.so \
    ${libdir}/ngf/libngfd_null.so \
    ${systemd_user_unitdir}/ngfd.service \
"

FILES:${PN}-dev += " \
    ${includedir}/ngf \
    ${libdir}/pkgconfig/ngf-plugin.pc \
"
