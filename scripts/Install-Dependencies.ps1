[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $PSScriptRoot

foreach ($app in @('apps/web', 'apps/admin-web')) {
    Push-Location (Join-Path $repoRoot $app)
    try {
        npm ci
        if ($LASTEXITCODE -ne 0) { throw "npm ci failed for $app" }
    } finally {
        Pop-Location
    }
}
