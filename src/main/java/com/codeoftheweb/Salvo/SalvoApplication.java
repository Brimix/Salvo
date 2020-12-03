package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.WebAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args){
		SpringApplication.run(SalvoApplication.class, args);
		System.out.println("Server is up!\n");
	}

	@Bean
	public CommandLineRunner initData(
			PlayerRepository player_rep,
			GameRepository game_rep,
			GamePlayerRepository gp_rep,
			ShipRepository ship_rep,
			SalvoRepository salvo_rep,
			ScoreRepository score_rep ){
		return (args) -> {
			Player P1 = new Player("Angela", "angie@proyecto.acc", passwordEncoder().encode("angie"));
			Player P2 = new Player("Brian", "brian@proyecto.acc", passwordEncoder().encode("brian"));
			Player P3 = new Player("Carlos", "charles@proyecto.acc", passwordEncoder().encode("charles"));
			Player P4 = new Player("Daniela", "dani@proyecto.acc", passwordEncoder().encode("dani"));

			Date current = new Date();
			Game G1 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G2 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G3 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G4 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G5 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G6 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G7 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G8 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));

			GamePlayer GP1 = new GamePlayer(P1, G1); GamePlayer GP2 = new GamePlayer(P2, G1);
			GamePlayer GP3 = new GamePlayer(P1, G2); GamePlayer GP4 = new GamePlayer(P2, G2);
			GamePlayer GP5 = new GamePlayer(P2, G3); GamePlayer GP6 = new GamePlayer(P4, G3);
			GamePlayer GP7 = new GamePlayer(P2, G4); GamePlayer GP8 = new GamePlayer(P1, G4);
			GamePlayer GP9 = new GamePlayer(P4, G5); GamePlayer GP10 = new GamePlayer(P1, G5);
			GamePlayer GP11 = new GamePlayer(P3, G6); GamePlayer GP12 = new GamePlayer();
			GamePlayer GP13 = new GamePlayer(P4, G7); GamePlayer GP14 = new GamePlayer();
			GamePlayer GP15 = new GamePlayer(P3, G8); GamePlayer GP16 = new GamePlayer(P4, G8);

			// Juego 1
			Ship S1 = new Ship("Destroyer", GP1, Arrays.asList("H2", "H3", "H4"));
			Ship S2 = new Ship("Submarine", GP1, Arrays.asList("E1", "F1", "G1"));
			Ship S3 = new Ship("Patrol Boat", GP1, Arrays.asList("B4", "B5"));
			Ship S4 = new Ship("Destroyer", GP2, Arrays.asList("B5", "C5", "D5"));
			Ship S5 = new Ship("Patrol Boat", GP2, Arrays.asList("F1", "F2"));
			// Juego 2
			Ship S6 = new Ship("Destroyer", GP3, Arrays.asList("B5", "C5", "D5"));
			Ship S7 = new Ship("Patrol Boat", GP3, Arrays.asList("C6", "C7"));
			Ship S8 = new Ship("Submarine", GP4, Arrays.asList("A2", "A3", "A4"));
			Ship S9 = new Ship("Patrol Boat", GP4, Arrays.asList("G6", "H6"));
			// Juego 3
			Ship S10 = new Ship("Destroyer", GP5, Arrays.asList("B5", "C5", "D5"));
			Ship S11 = new Ship("Patrol Boat", GP5, Arrays.asList("C6", "C7"));
			Ship S12 = new Ship("Submarine", GP6, Arrays.asList("A2", "A3", "A4"));
			Ship S13 = new Ship("Patrol Boat", GP6, Arrays.asList("G6", "H6"));
			// Juego 4
			Ship S14 = new Ship("Destroyer", GP7, Arrays.asList("B5", "C5", "D5"));
			Ship S15 = new Ship("Patrol Boat", GP7, Arrays.asList("C6", "C7"));
			Ship S16 = new Ship("Submarine", GP8, Arrays.asList("A2", "A3", "A4"));
			Ship S17 = new Ship("Patrol Boat", GP8, Arrays.asList("G6", "H6"));
			// Juego 5
			Ship S18 = new Ship("Destroyer", GP9, Arrays.asList("B5", "C5", "D5"));
			Ship S19 = new Ship("Patrol Boat", GP9, Arrays.asList("C6", "C7"));
			Ship S20 = new Ship("Submarine", GP10, Arrays.asList("A2", "A3", "A4"));
			Ship S21 = new Ship("Patrol Boat", GP10, Arrays.asList("G6", "H6"));
			// Juego 6
			Ship S22 = new Ship("Destroyer", GP11, Arrays.asList("B5", "C5", "D5"));
			Ship S23 = new Ship("Patrol Boat", GP11, Arrays.asList("C6", "C7"));
			// Juego 8
			Ship S24 = new Ship("Destroyer", GP15, Arrays.asList("B5", "C5", "D5"));
			Ship S25 = new Ship("Patrol Boat", GP15, Arrays.asList("C6", "C7"));
			Ship S26 = new Ship("Submarine", GP16, Arrays.asList("A2", "A3", "A4"));
			Ship S27 = new Ship("Patrol Boat", GP16, Arrays.asList("G6", "H6"));

			// Juego 1
			Salvo T1 = new Salvo(GP1, 1, Arrays.asList("B5", "C5", "F1"));
			Salvo T2 = new Salvo(GP1, 2, Arrays.asList("F2", "D5"));
			Salvo T3 = new Salvo(GP2, 1, Arrays.asList("B4", "B5", "B6"));
			Salvo T4 = new Salvo(GP2, 2, Arrays.asList("E1", "H3", "A2"));
			// Juego 2
			Salvo T5 = new Salvo(GP3, 1, Arrays.asList("A2", "A4", "G6"));
			Salvo T6 = new Salvo(GP3, 2, Arrays.asList("A3", "H6"));
			Salvo T7 = new Salvo(GP4, 1, Arrays.asList("B5", "D5", "C7"));
			Salvo T8 = new Salvo(GP4, 2, Arrays.asList("C5", "C6"));
			// Juego 3
			Salvo T9 = new Salvo(GP5, 1, Arrays.asList("G6", "H6", "A4"));
			Salvo T10 = new Salvo(GP5, 1, Arrays.asList("A2", "A3", "D8"));
			Salvo T11 = new Salvo(GP6, 1, Arrays.asList("H1", "H2", "H3"));
			Salvo T12 = new Salvo(GP6, 1, Arrays.asList("E1", "F2", "G3"));
			// Juego 4
			Salvo T13 = new Salvo(GP7, 1, Arrays.asList("A3", "A4", "F7"));
			Salvo T14 = new Salvo(GP7, 2, Arrays.asList("A2", "G6", "H6"));
			Salvo T15 = new Salvo(GP8, 1, Arrays.asList("B5", "C6", "H1"));
			Salvo T16 = new Salvo(GP8, 2, Arrays.asList("C5", "C7", "D5"));
			// Juego 5
			Salvo T17 = new Salvo(GP9, 1, Arrays.asList("A1", "A2", "A3"));
			Salvo T18 = new Salvo(GP9, 2, Arrays.asList("G6", "G7", "G8"));
			Salvo T19 = new Salvo(GP10, 1, Arrays.asList("B5", "B6", "C7"));
			Salvo T20 = new Salvo(GP10, 2, Arrays.asList("C6", "D6", "E6"));
			Salvo T21 = new Salvo(GP10, 3, Arrays.asList("H1", "H8"));

			Score K1 = GP1.getScore(1.0D);
			Score K2 = GP2.getScore(0.0D);
			Score K3 = GP3.getScore(0.5D);
			Score K4 = GP4.getScore(0.5D);
			Score K5 = GP5.getScore(1.0D);
			Score K6 = GP6.getScore(0.0D);

			// Saving to Repositories
			player_rep.saveAll(Arrays.asList(P1, P2, P3, P4));
			game_rep.saveAll(Arrays.asList(G1, G2, G3, G4, G5, G6, G7, G8));
			gp_rep.saveAll(Arrays.asList(
					 GP1,  GP2,  GP3,  GP4,  GP5,  GP6,  GP7,  GP8,  GP9, GP10,
					GP11, GP13, GP15, GP16));
			ship_rep.saveAll(Arrays.asList(
					 S1,  S2,  S3,  S4,  S5,  S6,  S7,  S8,  S9, S10,
					S11, S12, S13, S14, S15, S16, S17, S18, S19, S20,
					S21, S22, S23, S24, S25, S26, S27));
			salvo_rep.saveAll(Arrays.asList(
					 T1,  T2,  T3,  T4,  T5,  T6,  T7,  T8,  T9, T10,
					T11, T12, T13, T14, T15, T16, T17, T18, T19, T20,
					T21));
			score_rep.saveAll(Arrays.asList(
					K1,  K2,  K3,  K4,  K5,  K6
			));
		};
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				System.out.println("Player found!\n");
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				System.out.println("Player not found!\n");
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/api/**").permitAll()
			.antMatchers("/web/**").permitAll()
			.antMatchers("/**").hasAuthority("USER");
		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");
		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}