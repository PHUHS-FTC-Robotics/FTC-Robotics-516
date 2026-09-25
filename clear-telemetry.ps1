$Files = adb shell "find /sdcard -type f -name 'data_*.csv' 2>/dev/null"

foreach ($File in $Files) {
    $File = $File.Trim()

    if ($File) {
        Write-Host "Deleting $File"
        adb shell rm "$File"
    }
}

Write-Host "Telemetry files cleared."