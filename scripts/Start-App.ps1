[CmdletBinding()]
param(
    [Parameter(Mandatory)]
    [ValidateSet('web', 'api', 'admin-web', 'admin-api')]
    [string]$App
)

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $PSScriptRoot
$appPath = Join-Path $repoRoot "apps/$App"

Push-Location $appPath
try {
    if ($App -in @('web', 'admin-web')) {
        npm run dev
    } else {
        mvn spring-boot:run
    }

    if ($LASTEXITCODE -ne 0) { throw "Application $App exited with code $LASTEXITCODE" }
} finally {
    Pop-Location
}
