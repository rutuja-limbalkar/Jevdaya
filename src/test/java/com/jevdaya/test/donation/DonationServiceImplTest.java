package com.jevdaya.test.donation;

import com.jevdaya.Entity.Donation;
import com.jevdaya.repo.DonationRepository;
import com.jevdaya.serviceImpl.DonationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private DonationServiceImpl donationService;   // Your service impl

    private Donation donation;

    @BeforeEach
    void setUp() {
        donation = new Donation();
        donation.setName("Rutuja");
        donation.setEmail("rutuja@example.com");
        donation.setDonationAmount(1000.0);
        donation.setProductId(1L);
        donation.setStatus("SUCCESS");
    }

    @Test
    void testSaveDonation_Success() {
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);

        Donation saved = donationService.saveDonation(donation);

        assertNotNull(saved);
        assertEquals("Rutuja", saved.getName());
        assertEquals(1000.0, saved.getDonationAmount());
        verify(donationRepository, times(1)).save(any(Donation.class));
    }

    @Test
    void testGetAllDonations() {
        List<Donation> donationList = Arrays.asList(donation, new Donation());

        when(donationRepository.findAll()).thenReturn(donationList);

        List<Donation> result = donationService.getAllDonations();

        assertEquals(2, result.size());
        verify(donationRepository, times(1)).findAll();
    }

    @Test
    void testCreateOrder_ReturnsJson() {
        String result = donationService.createOrder(500.0);

        assertNotNull(result);
        assertTrue(result.contains("amount") || result.contains("error"));
    }
}