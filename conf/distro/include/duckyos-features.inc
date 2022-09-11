DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit bluez4"

DISTRO_FEATURES = "\
  alsa ext2 irda largefile xattr \
  opengl pci pcmcia usbhost usbgadget\
  bluetooth 3g wifi bluez5 nfc \
  nfs zeroconf ld-is-gold pam \
  systemd adb wayland polkit \
"

# Use vc4graphics for all raspberrypi MACHINEs
MACHINE_FEATURES:append:rpi = " vc4graphics"

VIRTUAL-RUNTIME_init_manager = "systemd systemd-compat-units systemd-machine-units"

VOLATILE_LOG_DIR = "no"
