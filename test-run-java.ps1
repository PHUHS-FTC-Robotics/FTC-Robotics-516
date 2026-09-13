param (
    [Parameter(Mandatory=$true)]
    [string]$FileName
)

$source = "java-test\org.firstinspires.ftc.teamcode\$FileName.java"
$className = "org.firstinspires.ftc.teamcode.$FileName"

Write-Host "Compiling $FileName.java..."

javac -d java-test $source

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Running $FileName..." -ForegroundColor Cyan
Write-Host ""

java -cp java-test $className