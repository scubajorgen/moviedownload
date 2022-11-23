/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.util.List;

/**
 *
 * @author jorgen
 */
public class MovieDatabasePage
{
    int page;
    int total_pages;
    int total_results;
    List<MovieDatabaseRecord> results;
}
