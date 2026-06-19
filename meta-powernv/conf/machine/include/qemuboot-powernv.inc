IMAGE_CLASSES += "qemuboot"

QB_SYSTEM_NAME = "qemu-system-ppc64"
QB_MACHINE = "-machine powernv"
QB_MEM = "-m 4096"
QB_DEFAULT_BIOS = "skiboot.lid"
QB_DEFAULT_FSTYPE = "cpio.xz"
QB_NETWORK_DEVICE = "-device e1000e,netdev=net0,mac=@MAC@"
