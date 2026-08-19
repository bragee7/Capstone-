Get-ChildItem -Path "D:\br_project\new_cap\backend\src" -Recurse -Filter "*.java" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    $newContent = $content -replace 'com\.internship\.platform', 'com.br.casevault'
    if ($content -ne $newContent) {
        Set-Content -Path $_.FullName -Value $newContent -NoNewline
        Write-Host "Updated: $($_.FullName)"
    }
}
Write-Host "Done."
