Write-Host "Activating script..."
& ".\.venv\Scripts\Activate.ps1"
Write-Host "Pulling telemetry..."
& ".\pull-telemetry.ps1"
jupyter lab