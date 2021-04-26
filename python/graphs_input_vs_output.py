from textwrap import wrap

import matplotlib.pyplot as plt
import numpy as np


APERTURE = [
    0.01,
    0.02,
    0.04
]


def internal_graph_input_vs_output(data, label, color):
    avg = [n[0] for n in data]
    stdev = [n[1] for n in data]

    plt.scatter(APERTURE, avg, label=label, color=color)

    for i, n in enumerate(stdev):
        plt.errorbar(APERTURE[i], avg[i], marker='_', yerr=n, xuplims=True, xlolims=True, ecolor='#000000')


def graph_input_vs_output(data, label):
    internal_graph_input_vs_output(data, label, '#1F77B4')

    plt.xlabel('Tamaño de la apertura (m)')
    plt.ylabel('Tiempo (s)')
    plt.xticks(APERTURE, rotation=45)
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def graph_all_inputs_vs_output(systems):
    internal_graph_input_vs_output(systems[0], 'N = 10', '#1F77B4')
    internal_graph_input_vs_output(systems[1], 'N = 50', '#FF7F0E')

    plt.xlabel('Tamaño de la apertura (m)')
    plt.ylabel('Tiempo (s)')
    plt.xticks(APERTURE, rotation=45)
    plt.legend(loc='upper right')
    plt.tight_layout()
    plt.show()
    plt.clf()


def wrap_title(title):
    return "\n".join(wrap(title, 60))


def transpose(output):
    result = []

    for system in output:
        system_result = []
        for v in range(len(APERTURE)):
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
                266.12519,
                149.75496,
                62.274,
            ],
            [
                291.11485,
                147.24495,
                57.38769,
            ],
            [
                299.37545,
                140.98362,
                30.96607,
            ],
            [
                246.22239,
                203.5003,
                53.37399,
            ],
            [
                266.13455,
                162.01777,
                64.61538,
            ],
        ],
        [
            [
                46.55538,
                25.87369,
                17.19962,
            ],
            [
                65.76195,
                34.20375,
                17.2394,
            ],
            [
                61.36961,
                17.04066,
                23.4639,
            ],
            [
                58.4966,
                26.87965,
                13.34989,
            ],
            [
                42.07445,
                25.30236,
                17.04376,
            ],
        ],
    ])


def get_avg_std(output):
    return np.average(output), np.std(output)


def main():
    systems = get_values()

    input_vs_output_systems = []
    for i, system in enumerate(systems):
        input_vs_output = []

        for t_index, _ in enumerate(APERTURE):
            outputs = system[t_index]

            output_result = get_avg_std(outputs)
            input_vs_output.append(output_result)

        graph_input_vs_output(input_vs_output, 'N = 10 & |V| = 0,01' if i == 0 else 'N = 50 & |V| = 0,07')
        input_vs_output_systems.append(input_vs_output)

    graph_all_inputs_vs_output(input_vs_output_systems)


if __name__ == '__main__':
    main()
