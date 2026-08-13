// ====== ТЕЛЕВИЗОР ======
findViewById(R.id.btnTvPower).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: ТВ Вкл/Выкл (Samsung)");
    irManager.tvPowerSamsung();
});

findViewById(R.id.btnTvVolUp).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Громкость +");
    irManager.tvVolumeUpSamsung();
});

findViewById(R.id.btnTvVolDown).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Громкость -");
    irManager.tvVolumeDownSamsung();
});

// ====== КОНДИЦИОНЕР ======
findViewById(R.id.btnAcPower).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Кондиционер Вкл/Выкл");
    irManager.acPowerGree();
});

findViewById(R.id.btnAcTempUp).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Температура +");
    irManager.acTempUp();
});

findViewById(R.id.btnAcTempDown).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Температура -");
    irManager.acTempDown();
});

// ====== XIAOMI BOX ======
findViewById(R.id.btnBoxPower).setOnClickListener(v -> {
    tvOutput.append("\n📤 Отправка: Xiaomi Mi Box Вкл/Выкл");
    // ПРАВИЛЬНЫЙ ВЫЗОВ: передаём паттерн из базы
    int[] pattern = IrCodeDatabase.getBoxCodes("xiaomi_box").get("power");
    irManager.sendCustomSignal(pattern);
});
