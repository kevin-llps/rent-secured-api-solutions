package fr.esgi.rent.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OwnerResourceTest {

    @Test
    void shouldGetOwners() {
        List<String> owners = List.of("Jean-Claude Dupont", "Guillaume Dupond", "Robert Lopez");

        OwnerResource ownerResource = new OwnerResource();

        List<String> result = ownerResource.getOwners();

        assertThat(result).containsExactlyInAnyOrderElementsOf(owners);
    }

}
