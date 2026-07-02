package com.example.myapplication.models;

import java.io.Serializable;

public class PcBuild implements Serializable {
    private Product cpu;
    private Product cpuCooler;
    private Product motherboard;
    private Product ram;
    private Product storage;
    private Product powerSupply;
    private Product gpu;
    private Product pcCase;

    public PcBuild() {}
    public Product getCpu() { return cpu; }
    public Product getCpuCooler() { return cpuCooler; }
    public Product getMotherboard() { return motherboard; }
    public Product getRam() { return ram; }
    public Product getStorage() { return storage; }
    public Product getPowerSupply() { return powerSupply; }
    public Product getGpu() { return gpu; }
    public Product getPcCase() { return pcCase; }
    public void setCpu(Product cpu) { this.cpu = cpu; }
    public void setCpuCooler(Product cpuCooler) { this.cpuCooler = cpuCooler; }
    public void setMotherboard(Product motherboard) { this.motherboard = motherboard; }
    public void setRam(Product ram) { this.ram = ram; }
    public void setStorage(Product storage) { this.storage = storage; }
    public void setPsu(Product powerSupply) { this.powerSupply = powerSupply; }
    public void setGpu(Product gpu) { this.gpu = gpu; }
    public void setPcCase(Product pcCase) { this.pcCase = pcCase; }

    public double getTotalPrice() {
        double total = 0;
        if (cpu != null) total += cpu.getPrice();
        if (cpuCooler != null) total += cpuCooler.getPrice();
        if (motherboard != null) total += motherboard.getPrice();
        if (ram != null) total += ram.getPrice();
        if (storage != null) total += storage.getPrice();
        if (powerSupply != null) total += powerSupply.getPrice();
        if (gpu != null) total += gpu.getPrice();
        if (pcCase != null) total += pcCase.getPrice();
        return total;
    }
}
