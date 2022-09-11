SUMMARY = "USB mode controller"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5f30f0716dfdd0d91eb439ebec522ec2"

PV = "0.86.0+git${SRCPV}"
SRCREV_FORMAT = "dbus-glib"
SRCREV = "6fe8a6dd2492adc1464fb8a764b477e4bdec010b"
SRCREV_dbus-glib = "d42176ae4763e5288ef37ea314fe58387faf2005"

DEPENDS = "glib-2.0 libcheck dbus kmod doxygen-native systemd libtool"
RDEPENDS:${PN} = "glib-2.0 lsof systemd"
RDEPENDS:${PN}-dev = "${PN}"

TARGET_CC_ARCH += "${LDFLAGS}"

inherit pkgconfig autotools-brokensep

SRC_URI = " \
    git://github.com/sailfishos/usb-moded.git;protocol=https;branch=master \
    git://github.com/sailfishos-mirror/dbus-glib.git;protocol=https;branch=master;destsuffix=git/dbus-gmain;name=dbus-glib \
    file://10-usb-moded-defaults.ini \
    file://usb-moded.service \
"

S = "${WORKDIR}/git"

EXTRA_OECONF:prepend = "--prefix=/usr \
    --enable-app-sync \
    --disable-meegodevlock \
    --disable-debug \
    --disable-connman \
    --enable-systemd \
    --disable-sailfish-access-control \
    --disable-static \
"

do_install() {
    cd "${S}"/
    make DESTDIR="${D}" install
    
    install -m 644 -D "${S}"/src/usb_moded-dbus.h "${D}"/${includedir}/usb-moded/usb_moded-dbus.h
    install -m 644 -D "${S}"/src/usb_moded-modes.h "${D}"/${includedir}/usb-moded/usb_moded-modes.h
    install -m 644 -D "${S}"/src/usb_moded-appsync-dbus.h "${D}"/${includedir}/usb-moded/usb_moded-appsync-dbus.h
    install -m 644 -D "${S}"/src/com.meego.usb_moded.xml "${D}"/${libdir}/dbus-1.0/include/com.meego.usb_moded.xml
    install -m 644 -D "${S}"/usb_moded.pc "${D}"/${libdir}/pkgconfig/usb_moded.pc
    install -m 644 -D "${S}"/debian/usb_moded.conf "${D}"/${sysconfdir}/dbus-1/system.d/usb_moded.conf
    install -d "${D}"/${sysconfdir}
    install -d "${D}"/${sysconfdir}/usb-moded
    install -d "${D}"/${sysconfdir}/usb-moded/run
    install -d "${D}"/${sysconfdir}/usb-moded/run-diag
    install -d "${D}"/${sysconfdir}/usb-moded/diag
    install -m 644 -D "${S}"/config/diag/* "${D}"/${sysconfdir}/usb-moded/diag/
    install -m 644 -D "${S}"/config/run/* "${D}"/${sysconfdir}/usb-moded/run/
    install -m 644 -D "${S}"/config/run-diag/* "${D}"/${sysconfdir}/usb-moded/run-diag/
    install -m 644 -D "${S}"/config/mass-storage-jolla.ini "${D}"/${sysconfdir}/usb-moded/
    install -m 644 -D "${S}"/../10-usb-moded-defaults.ini "${D}"/${sysconfdir}/usb-moded/
    install -d "${D}"/${sharedstatedir}/usb-moded

    ln -sf /run/usb-moded/udhcpd.conf "${D}"/${sysconfdir}/udhcpd.conf

    mkdir -p "${D}"/${sysconfdir}/modprobe.d/
    touch "${D}"/${sysconfdir}/modprobe.d/g_ether.conf
    #systemd stuff
    install -d ${D}${systemd_system_unitdir}/basic.target.wants/
    install -m 644 -D "${S}"/../usb-moded.service "${D}"/${systemd_system_unitdir}/usb-moded.service
    ln -s ../usb-moded.service ${D}${systemd_system_unitdir}/basic.target.wants/usb-moded.service
    install -m 644 -D "${S}"/systemd/usb-moded-args.conf "${D}"/var/lib/environment/usb-moded/usb-moded-args.conf
    install -m 755 -D "${S}"/systemd/turn-usb-rescue-mode-off "${D}"/${bindir}/turn-usb-rescue-mode-off
    install -m 644 -D "${S}"/systemd/usb-rescue-mode-off.service "${D}"/${systemd_system_unitdir}/usb-rescue-mode-off.service
    install -m 644 -D "${S}"/systemd/usb-rescue-mode-off.service "${D}"/${systemd_system_unitdir}/graphical.target.wants/usb-rescue-mode-off.service
    install -m 644 -D "${S}"/systemd/usb-moded.conf "${D}"/${sysconfdir}/tmpfiles.d/usb-moded.conf
    install -m 644 -D "${S}"/systemd/adbd-prepare.service "${D}"/${systemd_system_unitdir}/adbd-prepare.service
    install -m 644 -D "${S}"/systemd/adbd-prepare.service "${D}"/${systemd_system_unitdir}/graphical.target.wants/adbd-prepare.service
    install -m 744 -D "${S}"/systemd/adbd-functionfs.sh "${D}"/usr/sbin/adbd-functionfs.sh
    install -d "${D}"/usr/share/user-managerd/remove.d/
    install -m 744 -D "${S}"/scripts/usb_mode_user_clear.sh "${D}"/usr/share/user-managerd/remove.d/
}

FILES:${PN} += " \
    ${sysconfdir}/usb-moded/10-usb-moded-defaults.ini \
    ${sysconfdir}/udhcpd.ini \
    ${sysconfdir}/tmpfiles.d/usb-moded.conf \
    ${sysconfdir}/dbus-1/system.d/usb_moded.conf \
    ${sbindir}/usb_moded \
    ${sbindir}/usb_moded_util \
    ${systemd_system_unitdir}/basic.target.wants/usb-moded.service \
    ${systemd_system_unitdir}/graphical.target.wants/adbd-prepare.service \
    ${systemd_system_unitdir}/graphical.target.wants/usb-rescue-mode-off.service \
    ${systemd_system_unitdir}/usb-moded.service \
    ${systemd_system_unitdir}/adbd-prepare.service \
    ${systemd_system_unitdir}/usb-rescue-mode-off.service \
    /usr/share/user-managerd/remove.d/usb_mode_user_clear.sh \
"

FILES:${PN}-dev += " \
    ${libdir}/pkgconfig/usb_moded.pc \
    ${includedir}/usb-moded/* \
    ${libdir}/dbus-1.0/include/com.meego.usb_moded.xml \
"

FILES:${PN}-dbg += " \
    ${sbindir}/.debug/usb_moded_util \
    ${sbindir}/.debug/usb_moded \
"