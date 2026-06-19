# OpenEmbedded BSP layer for PowerNV (Non-Virtualized) platforms

## About

This layer provides

* `skiboot`: Firmware which loads the OS and provides some runtime services
* `linux-openpower`: A kernel intended to run the petitboot bootloader

Power systems have a lower firmware (Hostboot) that does low level system
initialization, like DRAM training, which is not included.

## Dependencies

* URI: `https://git.openembedded.org/openembedded-core`
    + branch: matched branches (e.g. `master`)
    + layer: `meta`

* URI: `https://git.openembedded.org/meta-openembedded`
    + branch: matched branches (e.g. `master`)
    + layer: `meta-oe`

* URI: `https://github.com/open-power/meta-openpower`
    + branch: matched branches (e.g. `master`)
    + layer: `meta-petitboot`

## Patches

https://github.com/open-power/meta-openpower

## Adding the meta-powernv layer to your build

Run `bitbake-layers add-layer meta-powernv`
