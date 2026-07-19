package am.wago.mongoproducer.service;

import am.wago.mongoproducer.model.*;
import am.wago.mongoproducer.repository.ComputerComponentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoInsertSchedulerService {

    private final ComputerComponentRepository repository;
    private final Random random = new Random();

    private static final List<ComponentTemplate> TEMPLATES = List.of(

        // ── CPU (6 записей) ────────────────────────────────────────────────────
        new ComponentTemplate("CPU","Intel","USA","support@intel.com",36,
            "2200 Mission College Blvd","Santa Clara","US","95054",
            "Core i9-13900K","LGA1700",125, 5.8,24,"DDR5", 2100,38500,"Cinebench R23",new BigDecimal("569.00")),
        new ComponentTemplate("CPU","Intel","USA","support@intel.com",36,
            "2200 Mission College Blvd","Santa Clara","US","95054",
            "Core i7-13700K","LGA1700",125, 5.4,16,"DDR5", 1980,30100,"Cinebench R23",new BigDecimal("409.00")),
        new ComponentTemplate("CPU","Intel","USA","support@intel.com",36,
            "2200 Mission College Blvd","Santa Clara","US","95054",
            "Core i5-13600K","LGA1700",125, 5.1,14,"DDR5", 1870,24200,"Cinebench R23",new BigDecimal("319.00")),
        new ComponentTemplate("CPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Ryzen 9 7950X","AM5",170, 5.7,16,"DDR5", 2050,39600,"Cinebench R23",new BigDecimal("699.00")),
        new ComponentTemplate("CPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Ryzen 7 7700X","AM5",105, 5.4,8,"DDR5", 1970,19800,"Cinebench R23",new BigDecimal("399.00")),
        new ComponentTemplate("CPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Ryzen 5 7600X","AM5",105, 5.3,6,"DDR5", 1900,15200,"Cinebench R23",new BigDecimal("299.00")),

        // ── GPU (6 записей) ────────────────────────────────────────────────────
        new ComponentTemplate("GPU","Nvidia","USA","support@nvidia.com",36,
            "2788 San Tomas Expy","Santa Clara","US","95051",
            "GeForce RTX 4090","PCIe 4.0 x16",450, 2.52,16384,"GDDR6X", 28000,63000,"3DMark TimeSpy",new BigDecimal("1599.00")),
        new ComponentTemplate("GPU","Nvidia","USA","support@nvidia.com",36,
            "2788 San Tomas Expy","Santa Clara","US","95051",
            "GeForce RTX 4080","PCIe 4.0 x16",320, 2.51,9728,"GDDR6X", 21000,46000,"3DMark TimeSpy",new BigDecimal("1199.00")),
        new ComponentTemplate("GPU","Nvidia","USA","support@nvidia.com",36,
            "2788 San Tomas Expy","Santa Clara","US","95051",
            "GeForce RTX 4070 Ti","PCIe 4.0 x16",285, 2.61,7680,"GDDR6X", 18000,38000,"3DMark TimeSpy",new BigDecimal("799.00")),
        new ComponentTemplate("GPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Radeon RX 7900 XTX","PCIe 4.0 x16",355, 2.50,12288,"GDDR6", 22000,48000,"3DMark TimeSpy",new BigDecimal("999.00")),
        new ComponentTemplate("GPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Radeon RX 7900 XT","PCIe 4.0 x16",315, 2.45,10752,"GDDR6", 19000,42000,"3DMark TimeSpy",new BigDecimal("799.00")),
        new ComponentTemplate("GPU","AMD","USA","support@amd.com",36,
            "2485 Augustine Dr","Santa Clara","US","95054",
            "Radeon RX 7800 XT","PCIe 4.0 x16",263, 2.43,3840,"GDDR6", 14000,28000,"3DMark TimeSpy",new BigDecimal("499.00")),

        // ── RAM (5 записей) ────────────────────────────────────────────────────
        new ComponentTemplate("RAM","Corsair","USA","support@corsair.com",24,
            "47100 Bayside Pkwy","Fremont","US","94538",
            "Vengeance DDR5-6000 32GB","DIMM",45, 6.0,2,"DDR5", 38,96,"AIDA64",new BigDecimal("129.00")),
        new ComponentTemplate("RAM","Corsair","USA","support@corsair.com",24,
            "47100 Bayside Pkwy","Fremont","US","94538",
            "Vengeance DDR5-5600 16GB","DIMM",38, 5.6,2,"DDR5", 34,88,"AIDA64",new BigDecimal("79.00")),
        new ComponentTemplate("RAM","Kingston","USA","support@kingston.com",24,
            "17600 Newhope St","Fountain Valley","US","92708",
            "Fury Beast DDR5-5200 32GB","DIMM",40, 5.2,2,"DDR5", 34,83,"AIDA64",new BigDecimal("109.00")),
        new ComponentTemplate("RAM","Kingston","USA","support@kingston.com",24,
            "17600 Newhope St","Fountain Valley","US","92708",
            "Fury Beast DDR4-3200 16GB","DIMM",35, 3.2,2,"DDR4", 28,51,"AIDA64",new BigDecimal("49.00")),
        new ComponentTemplate("RAM","G.Skill","Taiwan","support@gskill.com",24,
            "No. 18, Wuquan Rd","New Taipei","TW","248",
            "Trident Z5 DDR5-6400 32GB","DIMM",42, 6.4,2,"DDR5", 40,102,"AIDA64",new BigDecimal("149.00")),

        // ── Storage (5 записей) ────────────────────────────────────────────────
        new ComponentTemplate("Storage","Samsung","South Korea","support@samsung.com",60,
            "129 Samsung-ro","Suwon","KR","16677",
            "990 Pro 2TB NVMe","M.2 PCIe 4.0",8, 7.45,128,"NVMe", 7450,6900,"CrystalDiskMark",new BigDecimal("179.00")),
        new ComponentTemplate("Storage","Samsung","South Korea","support@samsung.com",60,
            "129 Samsung-ro","Suwon","KR","16677",
            "870 EVO 1TB SATA","2.5\" SATA",4, 0.56,64,"SATA", 560,530,"CrystalDiskMark",new BigDecimal("89.00")),
        new ComponentTemplate("Storage","WD","USA","support@westerndigital.com",60,
            "5601 Great Oaks Pkwy","San Jose","US","95119",
            "Black SN850X 2TB NVMe","M.2 PCIe 4.0",9, 7.30,128,"NVMe", 7300,6600,"CrystalDiskMark",new BigDecimal("149.00")),
        new ComponentTemplate("Storage","Seagate","USA","support@seagate.com",60,
            "47488 Kato Rd","Fremont","US","94538",
            "Barracuda 4TB HDD","3.5\" SATA",9, 0.21,8,"SATA", 190,190,"CrystalDiskMark",new BigDecimal("79.00")),
        new ComponentTemplate("Storage","Crucial","USA","support@crucial.com",36,
            "5600 N River Rd","Rosemont","US","60018",
            "P5 Plus 1TB NVMe","M.2 PCIe 4.0",7, 6.60,64,"NVMe", 6600,5000,"CrystalDiskMark",new BigDecimal("99.00")),

        // ── Motherboard (5 записей) ────────────────────────────────────────────
        new ComponentTemplate("Motherboard","ASUS","Taiwan","support@asus.com",36,
            "No. 15, Li-Te Rd","Taipei","TW","11490",
            "ROG Maximus Z790 Hero","LGA1700",90, 5.8,4,"DDR5", 20,90,"BAPCO CrossMark",new BigDecimal("629.00")),
        new ComponentTemplate("Motherboard","ASUS","Taiwan","support@asus.com",36,
            "No. 15, Li-Te Rd","Taipei","TW","11490",
            "TUF Gaming B650-Plus","AM5",75, 5.7,4,"DDR5", 16,78,"BAPCO CrossMark",new BigDecimal("249.00")),
        new ComponentTemplate("Motherboard","MSI","Taiwan","support@msi.com",36,
            "No. 69, Lide St","New Taipei","TW","235",
            "MEG X670E ACE","AM5",85, 5.7,4,"DDR5", 18,86,"BAPCO CrossMark",new BigDecimal("549.00")),
        new ComponentTemplate("Motherboard","MSI","Taiwan","support@msi.com",36,
            "No. 69, Lide St","New Taipei","TW","235",
            "MAG B760 Tomahawk","LGA1700",80, 5.6,4,"DDR5", 15,72,"BAPCO CrossMark",new BigDecimal("199.00")),
        new ComponentTemplate("Motherboard","Gigabyte","Taiwan","support@gigabyte.com",36,
            "No. 6, Baoqiang Rd","New Taipei","TW","231",
            "Z790 Aorus Master","LGA1700",88, 5.8,4,"DDR5", 19,88,"BAPCO CrossMark",new BigDecimal("489.00")),

        // ── PSU (5 записей) ────────────────────────────────────────────────────
        new ComponentTemplate("PSU","Corsair","USA","support@corsair.com",120,
            "47100 Bayside Pkwy","Fremont","US","94538",
            "RM850x 850W","ATX",850, 2.0,4,"80+ Gold", 90,87,"Cybenetics ETA",new BigDecimal("139.00")),
        new ComponentTemplate("PSU","Seasonic","Taiwan","support@seasonic.com",120,
            "No. 3, Gongye 2nd Rd","New Taipei","TW","236",
            "Focus GX-750 750W","ATX",750, 1.8,4,"80+ Gold", 91,88,"Cybenetics ETA",new BigDecimal("119.00")),
        new ComponentTemplate("PSU","EVGA","USA","support@evga.com",120,
            "2900 S Diablo Way","Tempe","US","85282",
            "SuperNOVA 1000 G6 1000W","ATX",1000, 2.1,6,"80+ Gold", 92,89,"Cybenetics ETA",new BigDecimal("189.00")),
        new ComponentTemplate("PSU","be quiet!","Germany","support@bequiet.com",60,
            "Gohrpunkt 6","Glinde","DE","21509",
            "Straight Power 11 650W","ATX",650, 1.6,3,"80+ Platinum", 93,90,"Cybenetics ETA",new BigDecimal("129.00")),
        new ComponentTemplate("PSU","Cooler Master","Taiwan","support@coolermaster.com",60,
            "No. 7, Fuhsing Rd","New Taipei","TW","236",
            "MWE 550 Bronze 550W","ATX",550, 1.5,2,"80+ Bronze", 87,84,"Cybenetics ETA",new BigDecimal("59.00"))
    );

    @PostConstruct
    public void seedAll() {
        log.info("Seeding {} components ({} per category)...", TEMPLATES.size(), "5-6");
        TEMPLATES.forEach(t -> {
            ComputerComponent saved = repository.save(build(t));
            log.info("  [{}] {} '{}' _id={}",
                    saved.getCategory(),
                    saved.getManufacturer().getName(),
                    saved.getSpecifications().getModel(),
                    saved.getId());
        });
        log.info("Seed complete: {} documents inserted across 6 categories " +
                "(CPU, GPU, RAM, Storage, Motherboard, PSU).", TEMPLATES.size());
    }

    @Scheduled(fixedRateString = "${app.producer.interval-ms:1800000}")
    public void insertComponent() {
        ComponentTemplate t = TEMPLATES.get(random.nextInt(TEMPLATES.size()));
        ComputerComponent saved = repository.save(build(t));
        log.info("Scheduled insert [{}] '{}' _id={}",
                saved.getCategory(), saved.getSpecifications().getModel(), saved.getId());
    }

    private ComputerComponent build(ComponentTemplate t) {
        return ComputerComponent.builder()
                .category(t.category())
                .price(t.price())
                .currency("USD")
                .manufacturer(Manufacturer.builder()
                        .name(t.brand())
                        .country(t.country())
                        .support(SupportInfo.builder()
                                .warrantyMonths(t.warrantyMonths())
                                .email(t.email())
                                .address(Address.builder()
                                        .street(t.street())
                                        .city(t.city())
                                        .country(t.addressCountry())
                                        .zipCode(t.zip())
                                        .build())
                                .build())
                        .build())
                .specifications(ComponentSpec.builder()
                        .model(t.model())
                        .socket(t.socket())
                        .tdpWatts(t.tdp())
                        .performance(PerformanceSpec.builder()
                                .clockSpeedGhz(t.clockGhz())
                                .coreCount(t.coreCount())
                                .memoryType(t.memoryType())
                                .benchmark(BenchmarkResult.builder()
                                        .singleCoreScore(t.singleCore())
                                        .multiCoreScore(t.multiCore())
                                        .testTool(t.testTool())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    private record ComponentTemplate(
            String category, String brand, String country, String email, int warrantyMonths,
            String street, String city, String addressCountry, String zip,
            String model, String socket, int tdp,
            double clockGhz, int coreCount, String memoryType,
            int singleCore, int multiCore, String testTool,
            BigDecimal price
    ) {}
}
