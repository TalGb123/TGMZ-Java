package com.example.myapplication.models;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String id;
    private String category;
    private String name;
    private Double price;
    private String brand;
    private String image;
    private Boolean inStock;
    private String color;
    private String type;
    private String socket;
    private Integer tdp;
    private String formFactor;
    private Double boostClock;
    private Double coreClock;
    private Integer coreCount;
    private Boolean hasApu;
    private List<String> supportedMemory;
    private List<String> supportedSockets;
    private Double height;
    private Integer radiatorSize;
    private Double noiseLevel;
    private Integer maxTdpCooling;
    private String memoryGen;
    private Integer memorySlots;
    private Boolean hasWifiBluetooth;
    private Integer m2Slots;
    private List<String> connections;
    private Integer vrmTier;
    private List<Object> speed;
    private List<Integer> modules;
    private Integer capacity;
    private String driveType;
    private String chipset;
    private Integer memory;
    private Double length;
    private Double slotsRequired;
    private Integer recommendedPsuWattage;
    private Double maxGpuLength;
    private Double maxCpuCoolerHeight;
    private String psuFormFactor;
    private List<Integer> supportedRadiators;
    private String sidepanelMaterial;
    private String efficiency;
    private Integer wattage;
    private String modular;
    private String rating;
    private String performanceTier;

    public Product() {}

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public void setTdp(Integer tdp) {
        this.tdp = tdp;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setBoostClock(Double boostClock) {
        this.boostClock = boostClock;
    }

    public void setCoreClock(Double coreClock) {
        this.coreClock = coreClock;
    }

    public void setCoreCount(Integer coreCount) {
        this.coreCount = coreCount;
    }

    public void setHasApu(Boolean hasApu) {
        this.hasApu = hasApu;
    }

    public void setSupportedSockets(List<String> supportedSockets) {
        this.supportedSockets = supportedSockets;
    }

    public void setSupportedMemory(List<String> supportedMemory) {
        this.supportedMemory = supportedMemory;
    }

    public void setRadiatorSize(Integer radiatorSize) {
        this.radiatorSize = radiatorSize;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setNoiseLevel(Double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public void setMaxTdpCooling(Integer maxTdpCooling) {
        this.maxTdpCooling = maxTdpCooling;
    }

    public void setMemoryGen(String memoryGen) {
        this.memoryGen = memoryGen;
    }

    public void setMemorySlots(Integer memorySlots) {
        this.memorySlots = memorySlots;
    }

    public void setHasWifiBluetooth(Boolean hasWifiBluetooth) {
        this.hasWifiBluetooth = hasWifiBluetooth;
    }

    public void setM2Slots(Integer m2Slots) {
        this.m2Slots = m2Slots;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public void setVrmTier(Integer vrmTier) {
        this.vrmTier = vrmTier;
    }

    public void setSpeed(List<Object> speed) {
        this.speed = speed;
    }

    public void setModules(List<Integer> modules) {
        this.modules = modules;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setSlotsRequired(Double slotsRequired) {
        this.slotsRequired = slotsRequired;
    }

    public void setRecommendedPsuWattage(Integer recommendedPsuWattage) {
        this.recommendedPsuWattage = recommendedPsuWattage;
    }

    public void setMaxGpuLength(Double maxGpuLength) {
        this.maxGpuLength = maxGpuLength;
    }

    public void setMaxCpuCoolerHeight(Double maxCpuCoolerHeight) {
        this.maxCpuCoolerHeight = maxCpuCoolerHeight;
    }

    public void setPsuFormFactor(String psuFormFactor) {
        this.psuFormFactor = psuFormFactor;
    }

    public void setSupportedRadiators(List<Integer> supportedRadiators) {
        this.supportedRadiators = supportedRadiators;
    }

    public void setSidepanelMaterial(String sidepanelMaterial) {
        this.sidepanelMaterial = sidepanelMaterial;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public void setWattage(Integer wattage) {
        this.wattage = wattage;
    }

    public void setModular(String modular) {
        this.modular = modular;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setPerformanceTier(String performanceTier) {
        this.performanceTier = performanceTier;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getImage() {
        return image;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public String getSocket() {
        return socket;
    }

    public Integer getTdp() {
        return tdp;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public Double getBoostClock() {
        return boostClock;
    }

    public Double getCoreClock() {
        return coreClock;
    }

    public Integer getCoreCount() {
        return coreCount;
    }

    public Boolean getHasApu() {
        return hasApu;
    }

    public List<String> getSupportedMemory() {
        return supportedMemory;
    }

    public List<String> getSupportedSockets() {
        return supportedSockets;
    }

    public Double getHeight() {
        return height;
    }

    public Integer getRadiatorSize() {
        return radiatorSize;
    }

    public Double getNoiseLevel() {
        return noiseLevel;
    }

    public Integer getMaxTdpCooling() {
        return maxTdpCooling;
    }

    public String getMemoryGen() {
        return memoryGen;
    }

    public Integer getMemorySlots() {
        return memorySlots;
    }

    public Boolean getHasWifiBluetooth() {
        return hasWifiBluetooth;
    }

    public Integer getM2Slots() {
        return m2Slots;
    }

    public List<String> getConnections() {
        return connections;
    }

    public Integer getVrmTier() {
        return vrmTier;
    }

    public List<Object> getSpeed() {
        return speed;
    }

    public List<Integer> getModules() {
        return modules;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDriveType() {
        return driveType;
    }

    public String getChipset() {
        return chipset;
    }

    public Integer getMemory() {
        return memory;
    }

    public Double getLength() {
        return length;
    }

    public Double getSlotsRequired() {
        return slotsRequired;
    }

    public Integer getRecommendedPsuWattage() {
        return recommendedPsuWattage;
    }

    public Double getMaxGpuLength() {
        return maxGpuLength;
    }

    public Double getMaxCpuCoolerHeight() {
        return maxCpuCoolerHeight;
    }

    public String getPsuFormFactor() {
        return psuFormFactor;
    }

    public List<Integer> getSupportedRadiators() {
        return supportedRadiators;
    }

    public String getSidepanelMaterial() {
        return sidepanelMaterial;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public Integer getWattage() {
        return wattage;
    }

    public String getModular() {
        return modular;
    }

    public String getRating() {
        return rating;
    }

    public String getPerformanceTier() {
        return performanceTier;
    }
}