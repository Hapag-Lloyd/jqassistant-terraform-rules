package com.hlag.jqassistant.terraform.constraint;

import static org.assertj.core.api.Assertions.assertThat;

import org.jqassistant.contrib.plugin.hcl.model.TerraformBlock;
import org.junit.jupiter.api.Test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.core.rule.api.model.Severity;
import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

public class ObjectNameTest extends AbstractJavaPluginIT {
    private static final String HYPHEN_TEST_FILE = "constraint/defaultObjectName/hyphen.tf";
    private static final String UPPERCASE_TEST_FILE = "constraint/defaultObjectName/uppercase.tf";

    @Test
    public void shouldFindObjectNamesContainingHyphen() throws RuleException {
        // given
        scanClassPathResource(DefaultScope.NONE, HYPHEN_TEST_FILE);

        // when
        final Result<Constraint> actualConstraint = validateConstraint("DefaultObjectNames");

        // then
        assertThat(actualConstraint.getSeverity()).isEqualTo(Severity.MAJOR);
        assertThat(actualConstraint.getStatus()).isEqualTo(Status.FAILURE);

        this.store.beginTransaction();

        assertThat(actualConstraint.getRows()).hasSize(1);

        final TerraformBlock actualInvalidObject = (TerraformBlock) actualConstraint.getRows().get(0).get("n");
        assertThat(actualInvalidObject.getInternalName()).isEqualTo("name-with-hyphen");

        this.store.rollbackTransaction();
    }

    @Test
    public void shouldFindObjectNamesContainingUppercaseLetters() throws RuleException {
        // given
        scanClassPathResource(DefaultScope.NONE, UPPERCASE_TEST_FILE);

        // when
        final Result<Constraint> actualConstraint = validateConstraint("hcl:ObjectNames");

        // then
        assertThat(actualConstraint.getSeverity()).isEqualTo(Severity.MAJOR);
        assertThat(actualConstraint.getStatus()).isEqualTo(Status.FAILURE);

        this.store.beginTransaction();

        assertThat(actualConstraint.getRows()).hasSize(1);

        final TerraformBlock actualInvalidObject = (TerraformBlock) actualConstraint.getRows().get(0).get("n");
        assertThat(actualInvalidObject.getInternalName()).isEqualTo("No_Uppercase_Letters");

        this.store.rollbackTransaction();
    }

}
