[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $PSScriptRoot

$legacy = @(
    'package.json',
    'src',
    'Los/pom.xml',
    'admin-web/package.json',
    'LosAdmin/pom.xml'
)

$monorepo = @(
    'apps/web/package.json',
    'apps/web/src',
    'apps/api/pom.xml',
    'apps/api/src',
    'apps/admin-web/package.json',
    'apps/admin-web/src',
    'apps/admin-api/pom.xml',
    'apps/admin-api/src'
)

function Get-MissingPaths {
    param([string[]]$Paths)

    return @($Paths | Where-Object {
        -not (Test-Path -LiteralPath (Join-Path $repoRoot $_))
    })
}

$legacyMissing = @(Get-MissingPaths -Paths $legacy)
$monorepoMissing = @(Get-MissingPaths -Paths $monorepo)

if ($monorepoMissing.Count -eq 0) {
    Write-Host 'PASS: enterprise monorepo structure is complete.'
    exit 0
}

if ($legacyMissing.Count -eq 0) {
    Write-Error 'FAIL: legacy project structure is still active.'
    exit 1
}

Write-Error ("FAIL: incomplete project structure. Missing: {0}" -f ($monorepoMissing -join ', '))
exit 1
