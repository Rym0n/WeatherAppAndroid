package com.example.weather;

public class TempInfo {

        private String feelTemp;
        private String minTemp;
        private String maxTemp;
        private String pressureTemp;
        private String humidityTemp;
        private String descriptionTemp;

        public String getFeelTemp() {
            return feelTemp;
        }

        public String getMinTemp() {
            return minTemp;
        }

        public String getMaxTemp() {
            return maxTemp;
        }

        public String getPressureTemp() {
            return pressureTemp;
        }

        public String getHumidityTemp() {
            return humidityTemp;
        }

        public String getDescriptionTemp() { return descriptionTemp; }

        public void setFeelTemp(String feelTemp) {
            this.feelTemp = feelTemp;
        }

        public void setMinTemp(String minTemp) {
            this.minTemp = minTemp;
        }

        public void setMaxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
        }

        public void setPressureTemp(String pressureTemp) {
            this.pressureTemp = pressureTemp;
        }

        public void setHumidityTemp(String humidityTemp) {
            this.humidityTemp = humidityTemp;
        }

        public void setDescriptionTemp(String descriptionTemp) { this.descriptionTemp = descriptionTemp; }

        public TempInfo(String feelTemp, String minTemp, String maxTemp, String pressureTemp, String humidityTemp, String descriptionTemp) {
            this.feelTemp = feelTemp;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.pressureTemp = pressureTemp;
            this.humidityTemp = humidityTemp;
            this.descriptionTemp = descriptionTemp;
        }

}
