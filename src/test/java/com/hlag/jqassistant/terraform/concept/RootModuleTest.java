package com.hlag.jqassistant.terraform.concept;

import static org.assertj.core.api.Assertions.assertThat;

import org.jqassistant.contrib.plugin.hcl.model.TerraformLogicalModule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.core.rule.api.model.Severity;
import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

public class RootModuleTest extends AbstractJavaPluginIT {
    private static final String ROOT_MODULE_MAIN_FILE = "/concept/root_module/main.tf";

    @Test
    @Disabled("Not working at the moment")
    public void shouldMarkTheTopLevelModuleAsRootModule() throws RuleException {
        // given
        scanClassPathResources(DefaultScope.NONE, ROOT_MODULE_MAIN_FILE);

        // when
        final Result<Concept> actualConcept = applyConcept("RootModule");

        // then
        assertThat(actualConcept.getSeverity()).isEqualTo(Severity.MINOR);
        assertThat(actualConcept.getStatus()).isEqualTo(Status.SUCCESS);

        this.store.beginTransaction();

        assertThat(actualConcept.getRows()).hasSize(1).first();

        final TerraformLogicalModule actualRootModule = (TerraformLogicalModule) actualConcept.getRows().get(0)
                .get("n");
        assertThat(actualRootModule.getFullQualifiedName()).isEqualTo(".concept.root-module");

        this.store.rollbackTransaction();
    }
}
