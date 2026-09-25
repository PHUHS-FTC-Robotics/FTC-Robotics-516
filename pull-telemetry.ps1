$ErrorActionPreference = "Stop"

$Destination = Join-Path $PSScriptRoot "data"

# Ensure the destination exists
New-Item -ItemType Directory -Force -Path $Destination | Out-Null

# Check ADB connection
Write-Host "Checking Robot Controller connection..."
adb devices

# Look for telemetry CSV files in shared storage
Write-Host "Searching for telemetry CSV files..."

$Files = adb shell "find /sdcard/FIRST/data -type f -name 'data_*.csv' 2>/dev/null"

if (-not $Files) {
    Write-Host "No telemetry CSV files found in shared storage."
    exit 1
}

# Pull each CSV into the project's data folder
foreach ($File in $Files) {
    $File = $File.Trim()

    if ($File) {
        Write-Host "Pulling $File"
        adb pull $File $Destination
    }
}

Write-Host "Telemetry transfer complete."