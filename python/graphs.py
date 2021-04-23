from textwrap import wrap

import matplotlib.pyplot as plt
import numpy as np


TEMP = [
    0.0001,
    0.000625,
    0.0016,
    0.003025,
    0.0049,
    0.007225,
    0.01,
]


def internal_graph_pressure_vs_temperature(data, label, color):
    avg = [n[0] for n in data]
    stdev = [n[1] for n in data]
    r = get_regression(TEMP, avg)

    plt.scatter(TEMP, avg, label=label, color=color)
    plt.plot(TEMP, r, label='Ajuste para ' + label, color=color)

    for i, n in enumerate(stdev):
        plt.errorbar(TEMP[i], avg[i], marker='_', yerr=n, xuplims=True, xlolims=True, ecolor='#000000')


def graph_pressure_vs_temperature(data, label):
    internal_graph_pressure_vs_temperature(data, label, '#1F77B4')

    plt.xlabel('Temperatura')
    plt.ylabel('Presion')
    plt.xticks(TEMP, rotation=45)
    plt.legend(loc='upper left')
    plt.tight_layout()
    plt.show()
    plt.clf()


def graph_all_pressure_vs_temperature(systems):
    internal_graph_pressure_vs_temperature(systems[0], 'N = 10', '#1F77B4')
    internal_graph_pressure_vs_temperature(systems[1], 'N = 50', '#FF7F0E')
    internal_graph_pressure_vs_temperature(systems[2], 'N = 100', '#2DA02D')

    plt.xlabel('Temperatura')
    plt.ylabel('Presion')
    plt.xticks(TEMP, rotation=45)
    plt.legend(loc='upper left')
    plt.tight_layout()
    plt.show()
    plt.clf()


def graph_regression_error(c, e_c, label):
    plt.plot(c, e_c, label='E(c) para ' + label, color='#FF0E0E')

    plt.xlabel('c')
    plt.ylabel('E(c)')
    plt.legend(loc='upper center')
    plt.tight_layout()
    plt.show()
    plt.clf()


def wrap_title(title):
    return "\n".join(wrap(title, 60))


def transpose(pressures):
    result = []

    for system in pressures:
        system_result = []
        for v in range(len(TEMP)):
            v_result = []
            for x in range(5):
                v_result.append(system[x][v])
            system_result.append(v_result)
        result.append(system_result)

    return result


def get_values():
    return transpose([
        [
            [
                0.025118622,
                0.17606963,
                0.217873364,
                0.803559417,
                0.892795952,
                1.621515848,
                2.168051063,
            ],
            [
                0.028219316,
                0.17606963,
                0.119632418,
                0.803559417,
                0.892795952,
                1.621515848,
                2.168051063,
            ],
            [
                0.027434823,
                0.17606963,
                0.217873364,
                0.803559417,
                0.892795952,
                1.621515848,
                2.168051063,
            ],
            [
                0.02839785,
                0.11118521,
                0.355704533,
                0.71264959,
                1.079663336,
                2.039425291,
                2.052227913,
            ],
            [
                0.037375649,
                0.162170337,
                0.217873364,
                0.344694707,
                1.192958328,
                1.660605156,
                1.98256402,
            ],
        ],
        [
            [
                0.124748686,
                0.636161007,
                1.833610364,
                3.938200302,
                4.348405005,
                10.2406119,
                12.0060085,
            ],
            [
                0.126317344,
                0.829005778,
                2.158842554,
                3.832656694,
                4.864257495,
                8.81467005,
                14.60423166,
            ],
            [
                0.125438678,
                0.87502315,
                2.057585333,
                3.467326958,
                5.502064459,
                9.348859309,
                12.93460361,
            ],
            [
                0.098498497,
                0.750732759,
                1.843458285,
                3.846502476,
                6.322597011,
                8.785913328,
                11.1770687,
            ],
            [
                0.092717096,
                0.794279313,
                2.007451757,
                2.576003519,
                6.741222082,
                8.449403901,
                11.97221772,
            ],
        ],
        [
            [
                0.197086067,
                1.307354909,
                3.812657316,
                8.911669222,
                11.90033753,
                15.76385295,
                28.41305608,
            ],
            [
                0.193430888,
                1.254738941,
                3.416930669,
                7.688023582,
                11.75249921,
                17.51294578,
                22.0401523,
            ],
            [
                0.200603874,
                1.713721057,
                2.973052568,
                6.925717598,
                10.12078819,
                18.80946167,
                24.00016462,

            ],
            [
                0.21255051,
                1.567713561,
                4.171981261,
                8.602228526,
                11.58945053,
                18.16776448,
                24.57569004,

            ],
            [
                0.258433985,
                1.483875122,
                4.152607907,
                8.871391758,
                12.41635328,
                14.59194657,
                28.77742864,
            ],
        ]
    ])


def regression_error(temps, pressures, c):
    result = 0

    for i in range(len(temps)):
        result += (pressures[i] - temps[i] * c) ** 2

    return result


def get_regression_min_c(temps, pressures):
    sum_p_t = 0
    sum_t2 = 0

    for i in range(len(temps)):
        sum_p_t += temps[i] * pressures[i]
        sum_t2 += temps[i] ** 2

    return sum_p_t / sum_t2


def get_regression(temps, pressures):
    return get_regression_min_c(temps, pressures) * np.array(temps) + pressures[0]


def get_avg_std(pressure):
    return np.average(pressure), np.std(pressure)


def main():
    systems = get_values()

    pressure_systems = []
    for i, system in enumerate(systems):
        pressure_vs_temperatures = []

        for t_index, temp in enumerate(TEMP):
            pressures = system[t_index]

            pressure_result = get_avg_std(pressures)
            pressure_vs_temperatures.append(pressure_result)

        avg = [n[0] for n in pressure_vs_temperatures]
        medium_c = get_regression_min_c(TEMP, avg)
        e_c = []
        c = []
        for n in np.linspace(medium_c - medium_c * 0.05, medium_c + medium_c * 0.05, 100):
            c.append(n)
            e_c.append(regression_error(TEMP, avg, n))

        graph_pressure_vs_temperature(pressure_vs_temperatures, 'N = 10' if i == 0 else ('N = 50' if i == 1 else 'N = 100'))
        graph_regression_error(c, e_c, 'N = 10' if i == 0 else ('N = 50' if i == 1 else 'N = 100'))
        print('c* = ' + str(medium_c) + ', E(c*) = ' + str(regression_error(TEMP, avg, medium_c)))
        pressure_systems.append(pressure_vs_temperatures)

    graph_all_pressure_vs_temperature(pressure_systems)


if __name__ == '__main__':
    main()
