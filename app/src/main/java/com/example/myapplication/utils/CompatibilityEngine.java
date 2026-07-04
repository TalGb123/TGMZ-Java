package com.example.myapplication.utils;

import com.example.myapplication.model.PcBuild;
import com.example.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates a product against the current PC build state.
 */
public class CompatibilityEngine {
    public static void evaluate(Product part, PcBuild build) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        Product cpu = build.getCpu();
        Product cooler = build.getCpuCooler();
        Product mobo = build.getMotherboard();
        Product ram = build.getRam();
        Product gpu = build.getGpu();
        Product pcCase = build.getPcCase();
        Product psu = build.getPowerSupply();

        String category = part.getCategory();

        // 1. MOTHERBOARD RULES
        if ("Motherboard".equals(category)) {
            if (cpu != null && part.getSocket() != null && !part.getSocket().equals(cpu.getSocket())) {
                errors.add("Socket mismatch: CPU needs " + cpu.getSocket() + ".");
            }
            if (ram != null && part.getMemoryGen() != null && ram.getSpeed() != null && !ram.getSpeed().isEmpty()) {
                if (!part.getMemoryGen().equals(ram.getSpeed().get(0).toString())) {
                    errors.add("Memory Gen mismatch: RAM is " + ram.getSpeed().get(0) + ".");
                }
            }
        }

        // 2. CPU RULES
        if ("CPU".equals(category)) {
            if (mobo != null && part.getSocket() != null && !part.getSocket().equals(mobo.getSocket())) {
                errors.add("Socket mismatch: Board requires " + mobo.getSocket() + ".");
            }
            if (cooler != null && cooler.getMaxTdpCooling() != null && part.getTdp() != null) {
                if (cooler.getMaxTdpCooling() < part.getTdp()) {
                    warnings.add("Thermal Warning: Cooler max TDP is below CPU TDP.");
                }
            }
        }

        // 3. MEMORY (RAM) RULES
        if ("Memory".equals(category)) {
            if (mobo != null && part.getSpeed() != null && !part.getSpeed().isEmpty()) {
                if (!part.getSpeed().get(0).toString().equals(mobo.getMemoryGen())) {
                    errors.add("Generation mismatch: Motherboard relies on " + mobo.getMemoryGen() + ".");
                }
            }
        }

        // 4. GPU & CASE RULES
        if ("VideoCard".equals(category)) {
            if (pcCase != null && pcCase.getMaxGpuLength() != null && part.getLength() != null) {
                if (part.getLength() > pcCase.getMaxGpuLength()) {
                    errors.add("Clearance issue: GPU exceeds case limit.");
                }
            }
        }

        if ("Case".equals(category)) {
            if (gpu != null && part.getMaxGpuLength() != null && gpu.getLength() != null) {
                if (part.getMaxGpuLength() < gpu.getLength()) {
                    errors.add("Clearance issue: Selected GPU is too long for this case.");
                }
            }
        }

        // 5. POWER SUPPLY RULES
        if ("PowerSupply".equals(category)) {
            int estimatedDraw = 100;
            if (cpu != null && cpu.getTdp() != null) estimatedDraw += cpu.getTdp();
            if (gpu != null && gpu.getTdp() != null) estimatedDraw += gpu.getTdp();

            if ((cpu != null || gpu != null) && part.getWattage() != null && part.getWattage() < estimatedDraw) {
                errors.add("Insufficient Power: Current selections draw ~" + estimatedDraw + "W.");
            }
        }

        // Apply Results
        if (!errors.isEmpty()) {
            part.setCompatible(false);
            part.setWarning(false);
            part.setCompatibilityReason("• " + String.join("\n• ", errors));
        } else if (!warnings.isEmpty()) {
            part.setCompatible(true);
            part.setWarning(true);
            part.setCompatibilityReason("• " + String.join("\n• ", warnings));
        } else {
            part.setCompatible(true);
            part.setWarning(false);
            part.setCompatibilityReason(null);
        }
    }
}
