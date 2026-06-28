Write-Host "== Pong WebFX build ==" -ForegroundColor Cyan

Write-Host "[1/3] Regenerando archivos WebFX..." -ForegroundColor Yellow
webfx update

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: webfx update falló." -ForegroundColor Red
    exit $LASTEXITCODE
}

$moduleInfoPath = ".\pong\src\main\java\module-info.java"

if (-not (Test-Path $moduleInfoPath)) {
    Write-Host "ERROR: No existe $moduleInfoPath" -ForegroundColor Red
    exit 1
}

Write-Host "[2/3] Aplicando parches en module-info.java..." -ForegroundColor Yellow

(Get-Content $moduleInfoPath) `
  -replace 'requires teavm\.jso;', 'requires org.teavm.jso;' `
  -replace '\s*opens sounds;\s*', '' |
  Set-Content $moduleInfoPath

Write-Host "Verificando module-info.java..." -ForegroundColor Yellow
Select-String -Path $moduleInfoPath -Pattern "teavm|sounds|provides"

Write-Host "[3/3] Compilando TeaVM JS..." -ForegroundColor Yellow
mvn clean package -Pteavm-js -pl pong-teavm-js -am

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: build falló." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Aplicando HTML personalizado con iframe..." -ForegroundColor Yellow

$targetDir = ".\pong-teavm-js\target\pong-teavm-js-1.0.0"
$generatedIndex = "$targetDir\index.html"
$gameIndex = "$targetDir\game.html"
$customIndex = ".\custom-web\index.html"

if (-not (Test-Path $generatedIndex)) {
    Write-Host "ERROR: No existe el index.html generado por WebFX: $generatedIndex" -ForegroundColor Red
    exit 1
}

Copy-Item $generatedIndex $gameIndex -Force
Write-Host "game.html generado desde el index de WebFX." -ForegroundColor Green

if (Test-Path $customIndex) {
    Copy-Item $customIndex $generatedIndex -Force
    Write-Host "index.html personalizado aplicado." -ForegroundColor Green
} else {
    Write-Host "ERROR: No existe $customIndex" -ForegroundColor Red
    exit 1
}

Write-Host "BUILD SUCCESS" -ForegroundColor Green
Write-Host ""
Write-Host "Para ejecutar:" -ForegroundColor Cyan
Write-Host "cd .\pong-teavm-js\target\pong-teavm-js-1.0.0"
Write-Host "python -m http.server 8080"
Write-Host ""
Write-Host "Abrir:"
Write-Host "http://localhost:8080"
