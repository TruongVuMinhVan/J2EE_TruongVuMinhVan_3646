@echo off
echo ========================================
echo   Starting Spring Boot Application...
echo ========================================
echo.
cd /d "%~dp0"

REM Kiểm tra xem đã build jar chưa
if not exist "target\TruongVuMinhVan_3646-0.0.1-SNAPSHOT.jar" (
    echo Building project first...
    call mvnw.cmd clean package -DskipTests
    if errorlevel 1 (
        echo Build failed! Please check errors above.
        pause
        exit /b 1
    )
)

REM Chạy ứng dụng bằng jar (ổn định hơn)
echo Starting application...
java -jar target\TruongVuMinhVan_3646-0.0.1-SNAPSHOT.jar

pause
