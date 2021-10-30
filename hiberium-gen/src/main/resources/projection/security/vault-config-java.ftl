package ${package_base};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.VaultConfigurer;
import org.springframework.context.annotation.Bean;

@Slf4j
@BootstrapConfiguration
public class VaultConfig {

    @Value("${r"${spring.cloud.vault.application-name}"}")
    private String vaultTargets;

    @Bean
    public VaultConfigurer configurer() {
        return new VaultConfigurer() {
            @Override
            public void addSecretBackends(SecretBackendConfigurer configurer) {
                String[] vaultPaths = vaultTargets.split(",");
                for(String vaultPath : vaultPaths) {
                    if(!vaultPath.isEmpty())
                        configurer.add("secret/"+vaultPath);
                }
                configurer.registerDefaultGenericSecretBackends(false);
                configurer.registerDefaultDiscoveredSecretBackends(false);
                log.info("Vault paths {}", Arrays.toString(vaultPaths));
            }
        };
    }
}