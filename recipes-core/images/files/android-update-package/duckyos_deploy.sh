#!/sbin/sh

target_dir=/data/duckyos
backup_dir=/data/duckyos_bak

tmp_extract=/data/duckyos_tmp_extract

backup() {
    mkdir -p $backup_dir
    if [ -d $2 ]; then
        echo "Removing previous backout of $1"
        rm -rf $2
    fi
    if [ -d $1 ]; then
        echo "Backing up $1 to $2"
        mv $1 $2
    fi
}

restore() {
    if [ -d $2 ]; then
        echo "Restoring $1 from $2"
        rm -rf $1
        mv $2 $1
    fi
}

deploy_duckyos() {
    echo "Remove firstboot flag"
    if [ -f /data/duckyos-data/.firstboot_done ]; then
        rm -f /data/duckyos-data/.firstboot_done
    fi

    echo "Deploying DuckyOS ..."
    if [ -d $tmp_extract ]; then
        rm -rf $tmp_extract
    fi
    mkdir $tmp_extract

    echo "Extracting /data/duckos-rootfs.tar.gz to $tmp_extract"
    /tmp/busybox-static tar --numeric-owner -xzf /data/duckyos-rootfs.tar.gz -C $tmp_extract
    if [ $? -ne 0 ] ; then
        echo "ERROR: Failed to extract DuckyOS on the internal memory. Propably not enough free space left to install DuckyOS?" >&2
        if ls $tmp_extract/lib/ld-linux-*.so.1 $tmp_extract/bin/busybox.nosuid >/dev/null 2>/dev/null; then
            echo "Trying with busybox already unpacked from duckyos-rootfs (hopefully)"
            rm -rf $tmp_extract-failed
            mv $tmp_extract $tmp_extract-failed
            mkdir $tmp_extract
            LD_LIBRARY_PATH=$tmp_extract-failed/lib/ $tmp_extract-failed/lib/ld-linux-*.so.1 $tmp_extract-failed/bin/busybox.nosuid tar -xzf /data/duckyos-rootfs.tar.gz -C $tmp_extract
            if [ $? -ne 0 ] ; then
                echo "ERROR: Failed to extract DuckyOS even with busybox from partially unpacked webos-rootfs, giving up"
                echo "Leaving $tmp_extract-failed behind, so that you can check what went wrong"
                exit 1
            else
                rm -rf $tmp_extract-failed
            fi
        else
            echo "ERROR: There isn't even partially unpacked $tmp_extract with $tmp_extract/lib/ld-linux-*.so.1 and $tmp_extract/bin/busybox.nosuid"
            if ls $target_dir/lib/ld-linux-*.so.1 $target_dir/bin/busybox.nosuid >/dev/null 2>/dev/null; then
                echo "Trying with busybox from previous duckyos installation in $target_dir"
                rm -rf $tmp_extract-failed
                rm -rf $tmp_extract
                mkdir $tmp_extract
                LD_LIBRARY_PATH=$target_dir/lib/ $target_dir/lib/ld-linux-*.so.1 $target_dir/bin/busybox.nosuid tar -xzf /data/duckyos-rootfs.tar.gz -C $tmp_extract
                if [ $? -ne 0 ] ; then
                    echo "ERROR: Failed to extract DuckyOS even with busybox from previous duckyos installation in $target_dir"
                    exit 1
                fi
            else
                echo "ERROR: There isn't busybox and ld-linux from previous duckyos installation in $target_dir/lib/ld-linux-*.so.1 and $target_dir/bin/busybox.nosuid"
                exit 1
            fi
        fi
    fi

    echo "Removing /data/duckyos-rootfs.tar.gz"
    rm /data/duckyos-rootfs.tar.gz
    if [ -d $target_dir ]; then
        echo "Removing old $target_dir"
        rm -rf $target_dir
    fi
    echo "Moving $tmp_extract to $target_dir"
    mv $tmp_extract $target_dir
    rm -rf $tmp_extract

    # Recreate symlink for Halium
    rm -rf /data/halium-rootfs
    if [ ! -e /data/halium-rootfs ]; then
        echo "Adding /data/halium-rootfs symlink to /data/duckyos"
        ln -sf duckyos /data/halium-rootfs
    fi

    echo "Done with deploying DuckyOS!!!"
}

deploy_duckyos

rm -rf $backup_dir
