param (
  [switch]$Overwrite,
  [string[]]$Arguments
)

$configFile = ".env"
$dataServerDir = "data-server"
$webScrapperDir = "web-scrapper"

function Show-Usage {
  Write-Host "Usage: .\create-config.ps1 [-Overwrite] key=value [key=value ...]"
  Write-Host "  -Overwrite  Overwrite existing config file if it exists"
  exit
}

if ($Arguments.Length -lt 1) {
  Show-Usage
}

$config = @{}
foreach ($arg in $Arguments) {
  if ($arg -match "=") {
    $key, $value = $arg -split "=", 2
    $config[$key] = $value
  }
  else {
    Write-Host "Error: Invalid argument format '$arg'. Expected key=value."
    Show-Usage
  }
}

function Update-Config {
  param (
    [string]$dir
  )
  $filePath = "$dir\$configFile"
  if (Test-Path $filePath) {
    if (-not $Overwrite) {
      Write-Host "Error: $filePath already exists. Use -Overwrite to overwrite."
      exit
    }
  }
  if (-not (Test-Path $dir)) {
    New-Item -ItemType Directory -Path $dir | Out-Null
  }
  $configText = $config.GetEnumerator() | ForEach-Object { "$($_.Key)=$($_.Value)" }
  $configText | Set-Content -Path $filePath
  Write-Host "Created $filePath"
}

Update-Config -dir $dataServerDir
Update-Config -dir $webScrapperDir
