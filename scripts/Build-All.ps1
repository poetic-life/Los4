[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $PSScriptRoot

foreach ($app in @('apps/web', 'apps/admin-web')) {
    Push-Location (Join-Path $repoRoot $app)
    try {
        npm run build
        if ($LASTEXITCODE -ne 0) { throw "Frontend build failed for $app" }
    } finally {
        Pop-Location
    }
}

foreach ($app in @('apps/api', 'apps/admin-api')) {
    Push-Location (Join-Path $repoRoot $app)
    try {
        mvn test
        if ($LASTEXITCODE -ne 0) { throw "Maven tests failed for $app" }
    } finally {
        Pop-Location
    }
}
