$ErrorActionPreference = "Stop"

# Check ADB connection
Write-Host "Checking Robot Controller connection..."
adb devices

# Look for telemetry CSV files in shared storage
Write-Host "Searching for telemetry CSV files to delete..."
$Files = adb shell "find /sdcard/FIRST/data -type f -name 'data_*.csv' 2>/dev/null"

if (-not $Files) {
    Write-Host "No telemetry CSV files found on the Robot Controller."
    exit 0
}

# Remove each CSV from the Robot Controller
foreach ($File in $Files) {
    $File = $File.Trim()
    if ($File) {
        Write-Host "Deleting $File..."
        adb shell "rm -f '$File'"
    }
}

Write-Host "Telemetry cleanup complete."
