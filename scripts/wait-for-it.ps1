param (
    [string]$Host = "localhost",
    [int]$Port = 1433,
    [int]$Timeout = 60,
    [string]$Command = ""
)

$StartTime = Get-Date
$EndTime = $StartTime.AddSeconds($Timeout)
$Connected = $false

Write-Host "Aguardando $Host:$Port ficar disponível (timeout $Timeout s)..."

while ((Get-Date) -lt $EndTime -and -not $Connected) {
    try {
        $tcp = New-Object System.Net.Sockets.TcpClient
        $tcp.Connect($Host, $Port)
        $Connected = $true
        $tcp.Close()
    } catch {
        Start-Sleep -Seconds 2
    }
}

if ($Connected) {
    Write-Host "$Host:$Port está pronto!"
    if ($Command -ne "") {
        Write-Host "Executando comando: $Command"
        Invoke-Expression $Command
    }
    exit 0
} else {
    Write-Host "Timeout atingido. $Host:$Port não respondeu."
    exit 1
}
